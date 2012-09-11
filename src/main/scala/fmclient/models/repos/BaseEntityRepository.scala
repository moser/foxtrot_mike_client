package fmclient.models.repos

import scalaj.collection.Imports._
import dispatch.json.JsHttp._
import dispatch.json.JsObject
import fmclient.models.{EntityMgr, BaseModel, Observer, Observalbe, Config, I18n}
import dispatch._
import scala.actors.Actor

case class SyncEvent(action : String, str : String, progress : Double)

abstract class BaseEntityRepository[T <: BaseModel[PKT], PKT](implicit m:scala.reflect.Manifest[T]) extends Observer with Observalbe {
  lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x ORDER BY " + orderBy)
  lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x").setMaxResults(1)

  val orderBy : String

  var dirty = true
  var cache : Seq[T] = _

  def find(id: PKT): T = EntityMgr.em.find(m.erasure, id).asInstanceOf[T]

  def extractId(o : JsObject) : PKT

  def all : Seq[T] = {
    if(dirty)
      cache = allQuery.getResultList.asInstanceOf[java.util.List[T]].asScala //.asInstanceOf[List[T]]
    cache
  }

  def first : Option[T] = {
    val r = firstQuery.getResultList
    if(r.size > 0) Option(r.get(0).asInstanceOf[T]) else None
  }

  def syncDown(username: String, password: String, progressUpdater : Actor) = {
    val http = new Http
    val req : Request = :/(Config.server, Config.port) / (toResource + ".json") as_!(username, password) gzip
    val remote = http(req ># (list ! obj))
    remote.foreach((o:JsObject) => {
      val id = extractId(o)
      if(find(id) == null) { //entity is new
        val r = m.erasure.getConstructor(classOf[JsObject]).newInstance(o).asInstanceOf[T]
        r.save
        progressUpdater ! SyncEvent(I18n("sync.create"), r.toString, (1.0 / remote.length.toDouble))
      } else {
        val r = find(id)
        r.update(o)
        r.save
        progressUpdater ! SyncEvent(I18n("sync.update"), r.toString, (1.0 / remote.length.toDouble))
      }
    })
    http.shutdown
  }

  def syncUp(username : String, password : String, progressUpdater : Actor) = {
    val http = new Http
    val req : Request = :/(Config.server, Config.port) / (toResource + ".json") << Map("json" -> "true") as_!(username, password) 
    val sync = all.filter(_.status == "local")
    sync.foreach(e => {
      http(req << Map(toJsonClass + "_json" -> e.toJson.toString) >- ((x : String) => {
        if(x == "OK") e.status = "synced"
      }))
      progressUpdater ! SyncEvent(I18n("sync.upload"), e.toString, (1.0 / sync.length.toDouble))
    })
    http.shutdown
  }

  def toResource =  toJsonClass + "s"
  def toJsonClass = m.erasure.getSimpleName.toLowerCase

  def created(a : BaseModel[_]) = {
    markDirty(a)
    notifyCreated(a)
  }

  def updated(a : BaseModel[_]) = {
    markDirty(a)
    notifyUpdated(a)
  }

  def removed(a : BaseModel[_]) = {
    markDirty(a)
    notifyRemoved(a)
  }

  private def markDirty(a : BaseModel[_]) = {
    a match {
      case model : T => {
        dirty = true
      }
      case _ => {}
    }
  }
}

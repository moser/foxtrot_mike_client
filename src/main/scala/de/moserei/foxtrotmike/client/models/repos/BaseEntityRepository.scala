package de.moserei.foxtrotmike.client.models.repos

import scalaj.collection.Imports._
import dispatch.json.JsHttp._
import dispatch.json.JsObject
import de.moserei.foxtrotmike.client.models.{EntityMgr, BaseModel, Observer, Observalbe}
import dispatch._
import scala.actors.Actor

abstract class BaseEntityRepository[T <: BaseModel](implicit m:scala.reflect.Manifest[T]) extends Observer with Observalbe {
  lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x")
  lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x").setMaxResults(1)
  
  var dirty = true
  var cache : Seq[T] = _

  def find(id: String): T = EntityMgr.em.find(m.erasure, id).asInstanceOf[T]

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
    val req : Request = :/("localhost", 3000) / (toResource + ".json") as(username, password)
    val remote = http(req ># (list ! obj)) map (Symbol(toJsonClass) ! obj)
    remote.foreach((o:JsObject) => {
      progressUpdater ! (1.0 / remote.length.toDouble)
      val id = ('id ! str)(o)
      if(find(id) == null) { //entity is new
        m.erasure.getConstructor(classOf[JsObject]).newInstance(o).asInstanceOf[T].save
      } else {
        val r = find(id)
        r.update(o)
        r.save
      }
    })
  }
  
  def syncUp(username : String, password : String, progressUpdater : Actor) = {
    val http = new Http
    val req : Request = :/("localhost", 3000) / (toResource + ".json") << Map("json" -> true) as(username, password) 
    val sync = all.filter(_.status == "local")
    sync.foreach(e => {
      progressUpdater ! (1.0 / sync.length.toDouble)
      http(req << Map(toJsonClass + "_json" -> e.toJson.toString) >- ((x : String) => {
        if(x == "OK") e.status = "synced"
      }))
    })
  }

  def toResource =  toJsonClass + "s"
  def toJsonClass = m.erasure.getSimpleName.toLowerCase
  
  def created(a : BaseModel) = {
    markDirty(a)
    notifyCreated(a)
  }
  
  def updated(a : BaseModel) = {
    markDirty(a)
    notifyUpdated(a)
  }
  
  def removed(a : BaseModel) = {
    markDirty(a)
    notifyRemoved(a)
  }
  
  private def markDirty(a : BaseModel) = {
    a match {
      case model : T => {
        dirty = true
      }
      case _ => {}
    }
  }
}

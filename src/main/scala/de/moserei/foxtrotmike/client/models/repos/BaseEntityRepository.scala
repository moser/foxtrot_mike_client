package de.moserei.foxtrotmike.client.models.repos

import scalaj.collection.Imports._
import dispatch.json.JsHttp._
import dispatch.json.JsObject
import de.moserei.foxtrotmike.client.models.{EntityMgr, BaseModel, Observer}
import dispatch._
import scala.actors.Actor

abstract class BaseEntityRepository[T <: BaseModel](implicit m:scala.reflect.Manifest[T]) extends Observer {
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
    println(password)
    val http = new Http
    val req : Request = :/("localhost", 3000) / (toResource + ".json") as(username, password)
    println(req.toString)
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

  def toResource =  toJsonClass + "s"
  def toJsonClass = m.erasure.getSimpleName.toLowerCase
  
  def update(a : BaseModel) = {
    a match {
      case airfield : T => {
        dirty = true
      }
      case _ => {}
    }
  }
}

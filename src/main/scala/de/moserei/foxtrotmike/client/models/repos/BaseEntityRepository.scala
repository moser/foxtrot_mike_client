package de.moserei.foxtrotmike.client.models.repos

import scalaj.collection.Imports._
import dispatch.json.JsHttp._
import dispatch.json.JsObject
import de.moserei.foxtrotmike.client.models.{EntityMgr, BaseModel}
import dispatch._

abstract class BaseEntityRepository[T <: BaseModel](implicit m:scala.reflect.Manifest[T]) {
  lazy val http = new Http
  lazy val req : Request = :/("localhost", 3000) / (toResource + ".json")

  lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x")
  lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x").setMaxResults(1)

  def find(id: String): T = EntityMgr.em.find(m.erasure, id).asInstanceOf[T]

  def all : Seq[T] = {
    allQuery.getResultList.asInstanceOf[java.util.List[T]].asScala //.asInstanceOf[List[T]]
  }

  def first : Option[T] = {
    val r = firstQuery.getResultList
    if(r.size > 0) Option(r.get(0).asInstanceOf[T]) else None
  }

  def syncDown = {
    val remote = http(req ># (list ! obj)) map (Symbol(toJsonClass) ! obj)
    remote.foreach((o:JsObject) => {
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
}
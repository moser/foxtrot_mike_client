package fmclient.models.repos

import scalaj.collection.Imports._
import fmclient.models.{EntityMgr, BaseModel, Observer, Observalbe, Config, I18n}
import net.liftweb.json._
import scalaj.http._

abstract class BaseEntityRepository[T <: BaseModel[PKT], PKT](implicit m:scala.reflect.Manifest[T]) extends Observer with Observalbe {

  lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x ORDER BY " + orderBy)
  lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x").setMaxResults(1)

  val orderBy : String

  var dirty = true
  var cache : Seq[T] = _

  def find(id: PKT): T = EntityMgr.em.find(m.erasure, id).asInstanceOf[T]

  def all : Seq[T] = {
    if(dirty)
      cache = allQuery.getResultList.asInstanceOf[java.util.List[T]].asScala
    cache
  }

  def first : Option[T] = {
    val r = firstQuery.getResultList
    if(r.size > 0) Option(r.get(0).asInstanceOf[T]) else None
  }

  def marshaller : Marshaller[T]

  def syncDown(username: String, password: String, sync : (String, String, Double) => Unit) = {
    val json = Http(s"http://${Config.server}:${Config.port}/${toResource}.json")
               .option(HttpOptions.connTimeout(10000))
               .option(HttpOptions.readTimeout(50000))
               .auth(username, password)
               .asString
    val pjson = parse(json)
    pjson match {
      case list : JArray => {
        val count = list.arr.length.toDouble
        list.arr.foreach((j) =>
          j match {
            case jsobj : JObject => {
              try {
              val obj = marshaller.unmarshalJson(jsobj)
              val existing = find(obj.id)
              if(existing != null) {
                marshaller.update(existing, obj)
                existing.status = "remote"
                existing.save
                sync(I18n("sync.update"), existing.toString, (1.0 / count))
              } else {
                obj.status = "remote"
                obj.save
                sync(I18n("sync.create"), obj.toString, (1.0 / count))
              }
              } catch {
                case e : Exception => {
                  println(e)
                  e.printStackTrace
                }
              }
            }
            case _ => throw new Exception("Expected js object")
          }
        )
      }
      case _ => throw new Exception("Expected js array")
    }
  }

  def modelsToSyncUp = all.filter(_.status == "local")

  def syncUp(username : String, password : String, sync : (String, String, Double) => Unit) = {
    val req = Http.post(s"http://${Config.server}:${Config.port}/${toResource}.json")
                  .option(HttpOptions.connTimeout(10000))
                  .option(HttpOptions.readTimeout(50000))
                  .auth(username, password)
                  .params("json" -> "true")
    val syncable = modelsToSyncUp
    val count = syncable.length.toDouble
    syncable.foreach(obj => {
      try {
        val res = req.params(toJsonClass + "_json" -> compact(render(marshaller.marshal(obj)))).asString
        obj.status = "synced"
        obj.save
        sync(I18n("sync.upload"), obj.toString, (1.0 / count))
      } catch {
        case e : Exception => {
          sync(I18n("error.unprocessable"), e.toString, (1.0 / count))
          throw e
        }
      }
    })
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

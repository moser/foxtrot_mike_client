package fmclient.models.repos

import dispatch.json.JsHttp._
import dispatch.json.JsObject
import fmclient.models.BaseModel

abstract class BaseStringIndexedEntityRepository[T <: BaseModel[String]](implicit m:scala.reflect.Manifest[T])  extends BaseEntityRepository[T, String] {
  def extractId(o : JsObject) = ('id ! str)(o)
}

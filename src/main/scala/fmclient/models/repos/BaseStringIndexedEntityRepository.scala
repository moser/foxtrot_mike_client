package fmclient.models.repos

import fmclient.models.BaseModel

abstract class BaseStringIndexedEntityRepository[T <: BaseModel[String]](implicit m:scala.reflect.Manifest[T])  extends BaseEntityRepository[T, String] {
}

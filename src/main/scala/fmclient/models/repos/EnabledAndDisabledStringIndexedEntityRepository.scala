package fmclient.models.repos

import scalaj.collection.Imports._
import fmclient.models.{EntityMgr, BaseModel}

abstract class EnabledAndDisabledStringIndexedEntityRepository[T <: BaseModel[String]](implicit m:scala.reflect.Manifest[T])  extends BaseStringIndexedEntityRepository[T] {
  lazy val enabledQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x WHERE x.disabled = false ORDER BY " + orderBy)
  lazy val disabledQuery = EntityMgr.em.createQuery("SELECT x FROM " + m.erasure.getSimpleName + " x WHERE x.disabled = true ORDER BY " + orderBy)

  var enabledCache = Seq[T]()
  var disabledCache = Seq[T]()

  def enabled : Seq[T] = {
    if(dirty)
      enabledCache = enabledQuery.getResultList.asInstanceOf[java.util.List[T]].asScala
    enabledCache
  }

  def disabled : Seq[T] = {
    if(dirty)
      disabledCache = disabledQuery.getResultList.asInstanceOf[java.util.List[T]].asScala
    disabledCache
  }
}

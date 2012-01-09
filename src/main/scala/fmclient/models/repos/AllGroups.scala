package fmclient.models.repos

import fmclient.models.{EntityMgr, Group}
import dispatch.json.JsHttp._
import dispatch.json.JsObject

object AllGroups extends BaseEntityRepository[Group, Int] {
  override def toResource = "groups"
  override val orderBy = "x.name ASC"
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Group x ORDER BY " + orderBy)
  override lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM Group x").setMaxResults(1)
  def extractId(o : JsObject) = ('id ! num)(o).intValue
}

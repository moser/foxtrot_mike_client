package de.moserei.foxtrotmike.client.models.repos

import de.moserei.foxtrotmike.client.models.{EntityMgr, Group}
import dispatch.json.JsHttp._
import dispatch.json.JsObject

object AllGroups extends BaseEntityRepository[Group, Int] {
  override def toResource = "groups"
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Group x")
  override lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM Group x").setMaxResults(1)
  
  def extractId(o : JsObject) = ('id ! num)(o).intValue
}

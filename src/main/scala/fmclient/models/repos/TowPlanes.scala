package fmclient.models.repos

import fmclient.models.{EntityMgr, Plane}

object TowPlanes extends BaseStringIndexedEntityRepository[Plane] {
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Plane x WHERE x.canTow = true")
  override lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM Plane x WHERE x.canTow = true").setMaxResults(1)
}
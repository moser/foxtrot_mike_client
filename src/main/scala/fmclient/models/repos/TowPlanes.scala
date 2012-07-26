package fmclient.models.repos

import fmclient.models.{EntityMgr, Plane}

object TowPlanes extends BaseStringIndexedEntityRepository[Plane] {
  override val orderBy = "x.registration ASC"
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Plane x WHERE x.canTow = true ORDER BY " + orderBy)
  override lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM Plane x WHERE x.canTow = true").setMaxResults(1)

  def created(a : Plane) = {
    if(a.canTow) {
      super.created(a)
    }
  }

  def updated(a : Plane) = {
    if(a.canTow) {
      super.updated(a)
    }
  }

  def removed(a : Plane) = {
    if(a.canTow) {
      super.removed(a)
    }
  }
}

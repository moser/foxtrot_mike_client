package fmclient.models.repos

import fmclient.models.{Plane, EntityMgr}

object AllPlanes extends EnabledAndDisabledStringIndexedEntityRepository[Plane] {
  override val orderBy = "x.disabled, x.registration ASC"
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Plane x WHERE x.deleted = false ORDER BY " + orderBy)
  override lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM Plane x WHERE x.deleted = false").setMaxResults(1)

  override val marshaller = GeneratedJsonConverters.PlaneMarshaller
}

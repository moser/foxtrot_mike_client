package fmclient.models.repos

import fmclient.models.{Airfield, EntityMgr}

object AllAirfields extends EnabledAndDisabledStringIndexedEntityRepository[Airfield] {
  override val orderBy = "x.disabled, x.name ASC"
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Airfield x WHERE x.deleted = false ORDER BY " + orderBy)
  override lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM Airfield x WHERE x.deleted = false").setMaxResults(1)

  override val marshaller = GeneratedJsonConverters.AirfieldMarshaller
}

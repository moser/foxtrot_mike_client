package fmclient.models.repos

import fmclient.models.{ Airfield, EntityMgr }

object AllAirfields extends BaseStringIndexedEntityRepository[Airfield] {
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Airfield x ORDER BY x.name ASC")
}

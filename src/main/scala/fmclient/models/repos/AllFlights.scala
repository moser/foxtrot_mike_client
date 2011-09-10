package fmclient.models.repos

import fmclient.models.{EntityMgr, Flight}

object AllFlights extends BaseStringIndexedEntityRepository[Flight] {
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Flight x ORDER BY x.pDepartureDate, x.pDepartureTime ASC")
}

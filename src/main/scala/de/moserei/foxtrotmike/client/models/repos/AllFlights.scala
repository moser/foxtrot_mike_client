package de.moserei.foxtrotmike.client.models.repos

import de.moserei.foxtrotmike.client.models.{EntityMgr, Flight}

object AllFlights extends BaseStringIndexedEntityRepository[Flight] {
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Flight x ORDER BY x.pDepartureDate, x.pDepartureTime ASC")
}

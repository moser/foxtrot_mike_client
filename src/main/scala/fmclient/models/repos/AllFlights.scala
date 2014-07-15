package fmclient.models.repos

import fmclient.models.{EntityMgr, Flight}

object AllFlights extends BaseStringIndexedEntityRepository[Flight] {
  override val orderBy = "x.pDepartureDate, x.pDepartureTime ASC"
  override val marshaller = FlightMarshaller

  override def modelsToSyncUp = super.modelsToSyncUp.filter(_.isValid)
}

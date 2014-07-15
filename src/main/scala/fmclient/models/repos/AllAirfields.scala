package fmclient.models.repos

import fmclient.models.{ Airfield, EntityMgr }

object AllAirfields extends EnabledAndDisabledStringIndexedEntityRepository[Airfield] {
  override val orderBy = "x.disabled, x.name ASC"
  override val marshaller = GeneratedJsonConverters.AirfieldMarshaller
}

package fmclient.models.repos

import fmclient.models.{ Airfield, EntityMgr }

object AllAirfields extends BaseStringIndexedEntityRepository[Airfield] {
  override val orderBy = "x.name ASC"
}

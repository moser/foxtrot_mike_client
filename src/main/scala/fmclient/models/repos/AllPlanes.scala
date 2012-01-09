package fmclient.models.repos

import fmclient.models.Plane

object AllPlanes extends BaseStringIndexedEntityRepository[Plane] {
  override val orderBy = "x.registration ASC"
}

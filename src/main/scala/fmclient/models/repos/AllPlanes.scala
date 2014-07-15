package fmclient.models.repos

import fmclient.models.Plane

object AllPlanes extends EnabledAndDisabledStringIndexedEntityRepository[Plane] {
  override val orderBy = "x.disabled, x.registration ASC"
  override val marshaller = GeneratedJsonConverters.PlaneMarshaller
}

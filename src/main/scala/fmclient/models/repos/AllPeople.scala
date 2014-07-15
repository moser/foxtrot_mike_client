package fmclient.models.repos

import fmclient.models.Person

object AllPeople extends EnabledAndDisabledStringIndexedEntityRepository[Person] {
  override def toResource = "people"
  override val orderBy = "x.disabled, x.lastname, x.firstname ASC"
  override val marshaller = GeneratedJsonConverters.PersonMarshaller
}

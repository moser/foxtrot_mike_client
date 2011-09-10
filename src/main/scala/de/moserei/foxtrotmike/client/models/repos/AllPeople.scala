package de.moserei.foxtrotmike.client.models.repos

import de.moserei.foxtrotmike.client.models.Person

object AllPeople extends BaseStringIndexedEntityRepository[Person] {
  override def toResource = "people"
}

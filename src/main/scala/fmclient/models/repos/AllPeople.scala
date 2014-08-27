package fmclient.models.repos

import fmclient.models.{EntityMgr, Person}

object AllPeople extends EnabledAndDisabledStringIndexedEntityRepository[Person] {
  override def toResource = "people"
  override val orderBy = "x.disabled, x.lastname, x.firstname ASC"
  override lazy val allQuery = EntityMgr.em.createQuery("SELECT x FROM Person x WHERE x.deleted = false ORDER BY " + orderBy)
  override lazy val firstQuery = EntityMgr.em.createQuery("SELECT x FROM Person x WHERE x.deleted = false").setMaxResults(1)

  override val marshaller = GeneratedJsonConverters.PersonMarshaller
}

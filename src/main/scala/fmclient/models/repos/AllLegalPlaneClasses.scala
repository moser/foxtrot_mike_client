package fmclient.models.repos

import fmclient.models.{ LegalPlaneClass, EntityMgr }

object AllLegalPlaneClasses extends BaseEntityRepository[LegalPlaneClass, Int] {
  override def toResource = "legal_plane_classes"
  override val orderBy = "x.name ASC"
  override val marshaller = GeneratedJsonConverters.LegalPlaneClassMarshaller
}

package fmclient.models.repos

import fmclient.models.{ LegalPlaneClass, EntityMgr }
import dispatch.json.JsHttp._
import dispatch.json.JsObject

object AllLegalPlaneClasses extends BaseEntityRepository[LegalPlaneClass, Int] {
  override def toResource = "legal_plane_classes"
  override val orderBy = "x.name ASC"
  def extractId(o : JsObject) = ('id ! num)(o).intValue
}

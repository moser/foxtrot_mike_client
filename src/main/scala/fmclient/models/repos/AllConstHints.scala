package fmclient.models.repos


import fmclient.models.{EntityMgr, CostHint}
import dispatch.json.JsHttp._
import dispatch.json.JsObject

object AllCostHints extends BaseEntityRepository[CostHint, Int] {
  override def toResource = "cost_hints"

  def extractId(o : JsObject) = ('id ! num)(o).intValue
}

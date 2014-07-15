package fmclient.models.repos


import fmclient.models.{EntityMgr, CostHint}

object AllCostHints extends BaseEntityRepository[CostHint, Int] {
  override def toResource = "cost_hints"
  override val orderBy = "x.name ASC"
  override val marshaller = GeneratedJsonConverters.CostHintMarshaller
}

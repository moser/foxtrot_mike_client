package fmclient.models

import javax.persistence._
import fmclient.models.repos.AllCostHints

@Entity
class CostHint extends BaseModel[Int] {
  addObserver(AllCostHints)

  @Id
  var id : Int = _
  var name : String = ""
  var status = "local"
  var disabled = false

  override def toString = {
    name
  }
}

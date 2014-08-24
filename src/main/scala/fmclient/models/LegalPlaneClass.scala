package fmclient.models

import javax.persistence._
import fmclient.models.repos.AllLegalPlaneClasses

@Entity
class LegalPlaneClass extends BaseModel[Int] {
  addObserver(AllLegalPlaneClasses)

  @Id
  var id : Int = _

  var name : String = ""
  var status = "local"
  var disabled = false

  override def toString = {
    name
  }
}

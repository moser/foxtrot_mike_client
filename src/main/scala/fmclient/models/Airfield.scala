package fmclient.models

import javax.persistence._
import fmclient.models.repos.AllAirfields

@Entity
class Airfield extends BaseModel[String] with UUIDHelper {
  addObserver(AllAirfields)

  @Id
  var id : String = createUUID

  var registration : String = ""
  var name : String = ""
  var status = "local"
  var disabled = false
  var deleted = false

  override def toString = {
    if(registration == null || registration.equals(""))
      name
    else
      registration
  }

  override def invalidFields = {
    var r = List[String]()
    if(nameValid) { r = r :+ "name" }
    r
  }

  def nameValid = name == null || name.length < 3
}

package fmclient.models

import javax.persistence._
import fmclient.models.repos.{AllPeople, AllGroups}

@Entity
class Person extends BaseModel[String] with UUIDHelper {
  addObserver(AllPeople)

  @Id
  var id : String = createUUID

  var firstname = ""
  var lastname = ""
  var status = "local"
  var disabled = false

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="group_id")
  var group : Group = _

  // Map of legal plane class IDs and the highest level
  // from the licenses.
  var licenseLevels = Map[Int, Int]()

  // Levels: 0 = Instructor, 1 = Normal, 2 = Trainee, default = 3 = No license at all
  def licenseLevel(legalPlaneClassId : Int, default : Int = 3) = licenseLevels.getOrElse(legalPlaneClassId, default)

  def name = lastname + " " + firstname
  override def toString = name

  override def invalidFields = {
    var r = List[String]()
    if(!firstnameValid) { r = r :+ "firstname" }
    if(!lastnameValid) { r = r :+ "lastname" }
    r
  }

  def firstnameValid = firstname != null && firstname.length > 2
  def lastnameValid = lastname != null && lastname.length > 2
}

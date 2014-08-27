package fmclient.models

import javax.persistence._
import fmclient.models.repos.{AllPlanes, AllGroups, AllLegalPlaneClasses, TowPlanes}

@Entity
class Plane extends BaseModel[String] with UUIDHelper {
  addObserver(AllPlanes)
  addObserver(TowPlanes)

  @Id
  var id : String = createUUID

  var registration  = ""
  var make = ""
  var status = "local"

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="group_id")
  var group : Group = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="legal_plane_class_id")
  var legalPlaneClass : LegalPlaneClass = _

  var seatCount = 1
  var competitionSign = ""
  var defaultLaunchMethod = ""
  var hasEngine = false
  var canFlyWithoutEngine = false
  var selflaunching = false
  var canTow = false
  var canBeTowed = false
  var canBeWireLaunched = false
  var defaultEngineDurationToDuration = false
  var disabled = false
  var deleted = false

  override def toString = registration

  def possibleLaunchMethods = {
    var r = List[String]()
    if(selflaunching) { r = r :+ "self_launch" }
    if(canBeTowed) { r = r :+ "tow_launch" }
    if(canBeWireLaunched) { r = r :+ "wire_launch" }
    r
  }

  override def invalidFields = {
    var r = List[String]()
    if(!registrationValid) { r = r :+ "registration" }
    if(!makeValid) { r = r :+ "make" }
    r
  }

  def registrationValid = registration != null && registration.length > 2
  def makeValid = make != null && make.length > 2
}

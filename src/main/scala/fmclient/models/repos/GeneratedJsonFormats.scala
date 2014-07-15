package fmclient.models.repos
import net.liftweb.json._
import fmclient.models._
import net.liftweb.json.JsonDSL._
abstract class Marshaller[T] {
  def unmarshalJson(j : JValue) : T
  def update(local : T, remote : T)
  def marshal(obj : T) : JValue
}
object GeneratedJsonConverters {
  implicit val formats = DefaultFormats
  object PlaneMarshaller extends Marshaller[Plane] {
    def marshal(o : Plane) = {
      (
        ("id" -> o.id) ~
        ("registration" -> o.registration) ~
        ("make" -> o.make) ~
        ("competition_sign" -> o.competitionSign) ~
        ("default_launch_method" -> o.defaultLaunchMethod) ~
        ("has_engine" -> o.hasEngine) ~
        ("can_fly_without_engine" -> o.canFlyWithoutEngine) ~
        ("selflaunching" -> o.selflaunching) ~
        ("can_tow" -> o.canTow) ~
        ("can_be_towed" -> o.canBeTowed) ~
        ("can_be_wire_launched" -> o.canBeWireLaunched) ~
        ("default_engine_duration_to_duration" -> o.defaultEngineDurationToDuration) ~
        ("disabled" -> o.disabled) ~
        ("legal_plane_class_id" -> o.legalPlaneClass.id) ~
        ("group_id" -> o.group.id)
      )
    }
    def update(local : Plane, remote : Plane) {
      assert(local.id == remote.id)
      local.id = remote.id
      local.registration = remote.registration
      local.make = remote.make
      local.competitionSign = remote.competitionSign
      local.defaultLaunchMethod = remote.defaultLaunchMethod
      local.hasEngine = remote.hasEngine
      local.canFlyWithoutEngine = remote.canFlyWithoutEngine
      local.selflaunching = remote.selflaunching
      local.canTow = remote.canTow
      local.canBeTowed = remote.canBeTowed
      local.canBeWireLaunched = remote.canBeWireLaunched
      local.defaultEngineDurationToDuration = remote.defaultEngineDurationToDuration
      local.disabled = remote.disabled
      local.legalPlaneClass = remote.legalPlaneClass
      local.group = remote.group
    }
    def unmarshalJson(value: JValue) = {
      val res = new Plane()
      res.id = (value \ "id").extract[String]
      res.registration = (value \ "registration").extract[String]
      res.make = (value \ "make").extract[String]
      res.competitionSign = (value \ "competition_sign").extract[String]
      res.defaultLaunchMethod = (value \ "default_launch_method").extract[String]
      res.hasEngine = (value \ "has_engine").extract[Boolean]
      res.canFlyWithoutEngine = (value \ "can_fly_without_engine").extract[Boolean]
      res.selflaunching = (value \ "selflaunching").extract[Boolean]
      res.canTow = (value \ "can_tow").extract[Boolean]
      res.canBeTowed = (value \ "can_be_towed").extract[Boolean]
      res.canBeWireLaunched = (value \ "can_be_wire_launched").extract[Boolean]
      res.defaultEngineDurationToDuration = (value \ "default_engine_duration_to_duration").extract[Boolean]
      res.disabled = (value \ "disabled").extract[Boolean]
      res.legalPlaneClass = AllLegalPlaneClasses.find((value \ "legal_plane_class_id").extract[Int])
      res.group = AllGroups.find((value \ "group_id").extract[Int])
      res
    }
  }
  object PersonMarshaller extends Marshaller[Person] {
    def marshal(o : Person) = {
      (
        ("id" -> o.id) ~
        ("firstname" -> o.firstname) ~
        ("lastname" -> o.lastname) ~
        ("group_id" -> o.group.id)
      )
    }
    def update(local : Person, remote : Person) {
      assert(local.id == remote.id)
      local.id = remote.id
      local.firstname = remote.firstname
      local.lastname = remote.lastname
      local.group = remote.group
    }
    def unmarshalJson(value: JValue) = {
      val res = new Person()
      res.id = (value \ "id").extract[String]
      res.firstname = (value \ "firstname").extract[String]
      res.lastname = (value \ "lastname").extract[String]
      res.group = AllGroups.find((value \ "group_id").extract[Int])
      res
    }
  }
  object WireLauncherMarshaller extends Marshaller[WireLauncher] {
    def marshal(o : WireLauncher) = {
      (
        ("id" -> o.id) ~
        ("registration" -> o.registration)
      )
    }
    def update(local : WireLauncher, remote : WireLauncher) {
      assert(local.id == remote.id)
      local.id = remote.id
      local.registration = remote.registration
    }
    def unmarshalJson(value: JValue) = {
      val res = new WireLauncher()
      res.id = (value \ "id").extract[String]
      res.registration = (value \ "registration").extract[String]
      res
    }
  }
  object GroupMarshaller extends Marshaller[Group] {
    def marshal(o : Group) = {
      (
        ("id" -> o.id) ~
        ("name" -> o.name)
      )
    }
    def update(local : Group, remote : Group) {
      assert(local.id == remote.id)
      local.id = remote.id
      local.name = remote.name
    }
    def unmarshalJson(value: JValue) = {
      val res = new Group()
      res.id = (value \ "id").extract[Int]
      res.name = (value \ "name").extract[String]
      res
    }
  }
  object LegalPlaneClassMarshaller extends Marshaller[LegalPlaneClass] {
    def marshal(o : LegalPlaneClass) = {
      (
        ("id" -> o.id) ~
        ("name" -> o.name)
      )
    }
    def update(local : LegalPlaneClass, remote : LegalPlaneClass) {
      assert(local.id == remote.id)
      local.id = remote.id
      local.name = remote.name
    }
    def unmarshalJson(value: JValue) = {
      val res = new LegalPlaneClass()
      res.id = (value \ "id").extract[Int]
      res.name = (value \ "name").extract[String]
      res
    }
  }
  object CostHintMarshaller extends Marshaller[CostHint] {
    def marshal(o : CostHint) = {
      (
        ("id" -> o.id) ~
        ("name" -> o.name)
      )
    }
    def update(local : CostHint, remote : CostHint) {
      assert(local.id == remote.id)
      local.id = remote.id
      local.name = remote.name
    }
    def unmarshalJson(value: JValue) = {
      val res = new CostHint()
      res.id = (value \ "id").extract[Int]
      res.name = (value \ "name").extract[String]
      res
    }
  }
  object AirfieldMarshaller extends Marshaller[Airfield] {
    def marshal(o : Airfield) = {
      (
        ("id" -> o.id) ~
        ("name" -> o.name) ~
        ("registration" -> o.registration)
      )
    }
    def update(local : Airfield, remote : Airfield) {
      assert(local.id == remote.id)
      local.id = remote.id
      local.name = remote.name
      local.registration = remote.registration
    }
    def unmarshalJson(value: JValue) = {
      val res = new Airfield()
      res.id = (value \ "id").extract[String]
      res.name = (value \ "name").extract[String]
      res.registration = (value \ "registration").extract[String]
      res
    }
  }
}

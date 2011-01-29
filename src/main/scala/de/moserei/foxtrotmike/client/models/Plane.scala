package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.{JsObject, JsString}
import dispatch.json.Js._
import de.moserei.foxtrotmike.client.models.repos.AllPlanes

@Entity
class Plane extends BaseModel with UUIDHelper {
  observers = AllPlanes :: observers
  
  @Id
  var id : String = createUUID

  var registration  = ""
  var make = ""
  var status = "local"
  
  var competitionSign = ""
  //var group, 
  var defaultLaunchMethod = ""
  var hasEngine = false 
  var canFlyWithoutEngine = false
  var canTow = false
  var canBeTowed = false
  var canBeWireLaunched = false
  var disabled = false

  override def toString = registration

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override protected def pUpdate(o:JsObject) = {
    registration = ('registration ! str)(o)
    make = ('make ! str)(o)
    //defaultLaunchMethod = ('default_launch_method ! str)(o)
    hasEngine = ('has_engine ! bool)(o)
    canFlyWithoutEngine = ('can_fly_without_engine ! bool)(o)
    canTow = ('can_tow ! bool)(o)
    canBeTowed = ('can_be_towed ! bool)(o)
    canBeWireLaunched = ('can_be_wire_launched ! bool)(o)
    disabled = ('disabled ! bool)(o)
  }
  
  override def jsonValues = {
    Map(JsString("registration") -> JsString(registration),
        JsString("make") -> JsString(make))
  }
}

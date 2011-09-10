package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.{JsObject, JsString}
import dispatch.json.Js._
import de.moserei.foxtrotmike.client.models.repos.{AllPlanes, AllGroups}
import de.moserei.foxtrotmike.client.models.Group

@Entity
class Plane extends BaseModel[String] with UUIDHelper {
  addObserver(AllPlanes)
  
  @Id
  var id : String = createUUID

  var registration  = ""
  var make = ""
  var status = "local"
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="group_id")
  var group : Group = _
  
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
    group = AllGroups.find(('group_id ! num)(o).intValue)
  }
  
  override def jsonValues = {
    Map(JsString("registration") -> JsString(registration),
        JsString("make") -> JsString(make),
        JsString("group_id") -> idToJsonInt(group))
  }
}

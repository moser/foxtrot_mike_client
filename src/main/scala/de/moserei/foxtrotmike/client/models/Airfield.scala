package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.{JsObject, JsString}
import dispatch.json.Js._
import de.moserei.foxtrotmike.client.models.repos.AllAirfields

@Entity
class Airfield extends BaseModel with UUIDHelper {
  observers = AllAirfields :: observers
  
  @Id
  var id : String = createUUID

  var registration : String = ""
  var name : String = ""  
  var status = "local"

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override protected def pUpdate(o:JsObject) = {
    name = ('name ?? str)(o).orNull
    registration = ('registration ?? str)(o).orNull
  }

  override def toString = {
    if(registration == null || registration.equals(""))
      name
    else
      registration
  }
  
  override def jsonValues = {
    Map(JsString("registration") -> stringToJson(registration),
        JsString("name") -> stringToJson(name))
  }
}

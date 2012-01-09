package fmclient.models

import javax.persistence._
import dispatch.json.{JsObject, JsString}
import dispatch.json.Js._
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

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override protected def pUpdate(o:JsObject) = {
    name = ('name ?? str)(o).orNull
    registration = ('registration ?? str)(o).orNull
    disabled = ('disabled ! bool)(o)
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

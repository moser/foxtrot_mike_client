package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.JsObject
import dispatch.json.Js._

@Entity
class Airfield extends BaseModel with UUIDHelper {
  @Id
  var id : String = createUUID

  var registration : String = ""
  var name : String = ""

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
}

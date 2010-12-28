package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.JsObject
import dispatch.json.Js._

@Entity
class Plane extends BaseModel with UUIDHelper {
  @Id
  var id : String = createUUID

  var registration  = ""
  var make = ""

  override def toString = registration

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override protected def pUpdate(o:JsObject) = {
    registration = ('registration ! str)(o)
    make = ('make ! str)(o)
  }
}

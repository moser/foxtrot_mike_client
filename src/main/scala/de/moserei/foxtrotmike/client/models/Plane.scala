package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.JsObject
import dispatch.json.Js._

@Entity
class Plane extends BaseModel with UUIDHelper {
  @Id
  var id : String = createUUID

  var registration : String = ""

  override def toString = registration

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override def update(o:JsObject) = {
    registration = ('registration ! str)(o)
  }
}

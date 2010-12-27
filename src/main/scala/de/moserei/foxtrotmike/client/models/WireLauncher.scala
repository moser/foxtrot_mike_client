package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.JsObject
import dispatch.json.Js._

@Entity
class WireLauncher extends BaseModel with UUIDHelper {
  @Id
  var id = createUUID
  var registration : String = ""

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override def update(o:JsObject) = {
    registration = ('registration ! str)(o)
  }
}
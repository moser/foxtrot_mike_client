package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.JsObject
import dispatch.json.Js._
import de.moserei.foxtrotmike.client.models.repos.AllWireLaunchers

@Entity
class WireLauncher extends BaseModel with UUIDHelper {
  observers = AllWireLaunchers :: observers

  @Id
  var id = createUUID
  var registration : String = ""

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override protected def pUpdate(o:JsObject) = {
    registration = ('registration ! str)(o)
  }

  override def toString = registration
}

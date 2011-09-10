package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.{JsObject, JsString, JsValue}

@Entity
@Inheritance
abstract class Launch extends BaseModel[String] with UUIDHelper {
  @Id
  var id = createUUID

  @OneToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="flight_id")
  var flight : Flight = _
  var status = "local"

  override protected def pUpdate(o:JsObject) = {} //launches are not synced down
  override def jsonValues = {
    Map[JsString, JsValue]()
  }
}

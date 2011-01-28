package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.JsObject

@Entity
@Inheritance
abstract class Launch extends BaseModel with UUIDHelper {
  @Id
  var id = createUUID

  @OneToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="flight_id")
  var flight : Flight = _

  override protected def pUpdate(o:JsObject) = {} //launches are not synced down
}

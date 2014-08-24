package fmclient.models

import javax.persistence._

@Entity
@Inheritance
abstract class Launch extends BaseModel[String] with UUIDHelper {
  @Id
  var id = createUUID

  @OneToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="flight_id")
  var flight : Flight = _
  var status = "local"
  var disabled = false
}

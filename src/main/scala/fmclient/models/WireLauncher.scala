package fmclient.models

import javax.persistence._
import fmclient.models.repos.AllWireLaunchers

@Entity
class WireLauncher extends BaseModel[String] with UUIDHelper {
  addObserver(AllWireLaunchers)

  @Id
  var id = createUUID
  var registration = ""
  var status = "local"

  override def toString = registration
}

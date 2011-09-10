package fmclient.models.repos

import fmclient.models.WireLauncher

object AllWireLaunchers extends BaseStringIndexedEntityRepository[WireLauncher] {
  override def toJsonClass = "wire_launcher"
}

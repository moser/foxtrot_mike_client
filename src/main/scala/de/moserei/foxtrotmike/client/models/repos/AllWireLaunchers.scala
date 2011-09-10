package de.moserei.foxtrotmike.client.models.repos

import de.moserei.foxtrotmike.client.models.WireLauncher

object AllWireLaunchers extends BaseStringIndexedEntityRepository[WireLauncher] {
  override def toJsonClass = "wire_launcher"
}

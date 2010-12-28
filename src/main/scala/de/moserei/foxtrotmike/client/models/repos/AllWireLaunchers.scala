package de.moserei.foxtrotmike.client.models.repos

import de.moserei.foxtrotmike.client.models.WireLauncher

object AllWireLaunchers extends BaseEntityRepository[WireLauncher] {
  override def toJsonClass = "wire_launcher"
}
package fmclient.models

import java.util.UUID

trait UUIDHelper {
  def createUUID = {
    UUID.randomUUID.toString
  }
}

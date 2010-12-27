package de.moserei.foxtrotmike.client.models

import org.joda.time.DateTime
import de.moserei.foxtrotmike.client.models.repos._

class Defaults {
  var date = (new DateTime).toDateMidnight.toDateTime
  var airfield = AllAirfields.first.orNull
  var towPlane = AllPlanes.first.orNull
  var controller = AllPeople.first.orNull
  var wireLauncher = AllWireLaunchers.first.orNull
}




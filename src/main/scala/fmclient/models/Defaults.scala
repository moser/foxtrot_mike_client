package fmclient.models

import org.joda.time.DateTime
import fmclient.models.repos._

class Defaults {
  var date = (new DateTime).toDateMidnight.toDateTime.toDate
  var airfield = AllAirfields.first.orNull
  var towPlane = TowPlanes.first.orNull
  var controller = AllPeople.first.orNull
  var wireLauncher = AllWireLaunchers.first.orNull
  var operator = AllPeople.first.orNull
  var towPilot = AllPeople.first.orNull
}




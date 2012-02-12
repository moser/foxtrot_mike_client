package fmclient.models

import org.joda.time.DateTime
import fmclient.models.repos._
import scala.swing.Publisher
import scala.swing.event._

object Defaults {
  case class Updated() extends Event
}

class Defaults extends Publisher {
  var date = (new DateTime).toDateMidnight.toDateTime.toDate
  var airfield = AllAirfields.first.orNull
  var towPlane = TowPlanes.first.orNull
  var towPilot = AllPeople.first.orNull
  var controller = AllPeople.first.orNull
  var wireLauncher = AllWireLaunchers.first.orNull
  var operator = AllPeople.first.orNull

  def update {
    airfield = Option(airfield).getOrElse(AllAirfields.first.orNull)
    towPlane = Option(towPlane).getOrElse(TowPlanes.first.orNull)
    towPilot = Option(towPilot).getOrElse(AllPeople.first.orNull)
    controller = Option(controller).getOrElse(AllPeople.first.orNull)
    wireLauncher = Option(wireLauncher).getOrElse(AllWireLaunchers.first.orNull)
    operator = Option(operator).getOrElse(AllPeople.first.orNull)

    publish(Defaults.Updated())
  }

}




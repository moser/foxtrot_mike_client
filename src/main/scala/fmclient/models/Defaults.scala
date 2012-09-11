package fmclient.models

import org.joda.time.DateTime
import fmclient.models.repos._
import fmclient.models._
import scala.swing.Publisher
import scala.swing.event._

object Defaults {
  case class Updated() extends Event
}

class Defaults extends Publisher {
  var date = (new DateTime).toDateMidnight.toDateTime.toDate

  private var _airfield = Option(AllAirfields.find(AllAttributes.read("defaultAirfieldId"))).getOrElse(AllAirfields.first.orNull)
  def airfield = _airfield
  def airfield_=(value : Airfield) = {
    _airfield = value
    if(value != null)
      AllAttributes.write("defaultAirfieldId", value.id)
  }

  private var _towPlane = Option(TowPlanes.find(AllAttributes.read("defaultTowPlaneId"))).getOrElse(TowPlanes.first.orNull)
  def towPlane = _towPlane
  def towPlane_=(value : Plane) = {
    _towPlane = value
    if(value != null)
      AllAttributes.write("defaultTowPlaneId", value.id)
  }

  private var _towPilot = Option(AllPeople.find(AllAttributes.read("defaultTowPilotId"))).getOrElse(AllPeople.first.orNull)
  def towPilot = _towPilot
  def towPilot_=(value : Person) = {
    _towPilot = value
    if(value != null)
      AllAttributes.write("defaultTowPilotId", value.id)
  }

  private var _controller = Option(AllPeople.find(AllAttributes.read("defaultControllerId"))).getOrElse(AllPeople.first.orNull)
  def controller = _controller
  def controller_=(value : Person) = {
    _controller = value
    if(value != null)
      AllAttributes.write("defaultControllerId", value.id)
  }

  private var _operator = Option(AllPeople.find(AllAttributes.read("defaultOperatorId"))).getOrElse(AllPeople.first.orNull)
  def operator = _operator
  def operator_=(value : Person) = {
    _operator = value
    if(value != null)
      AllAttributes.write("defaultOperatorId", value.id)
  }

  private var _wireLauncher = Option(AllWireLaunchers.find(AllAttributes.read("defaultWireLauncherId"))).getOrElse(AllWireLaunchers.first.orNull)
  def wireLauncher = _wireLauncher
  def wireLauncher_=(value : WireLauncher) = {
    _wireLauncher = value
    if(value != null)
      AllAttributes.write("defaultWireLauncherId", value.id)
  }

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




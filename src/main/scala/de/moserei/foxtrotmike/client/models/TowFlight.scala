package de.moserei.foxtrotmike.client.models

import javax.persistence._
import java.util.Date
import dispatch.json.{JsObject, JsString}

@Entity
@DiscriminatorValue("T")
class TowFlight extends AbstractFlight {
  def this(launch0: TowLaunch) {
    this()
    towLaunch = launch0
  }
  
  def this(launch0: TowLaunch, d : Defaults) = {
    this(launch0)
    plane = d.towPlane
    seat1 = d.towPilot
    to = d.airfield
  }
  
  def this(launch0: TowLaunch, f : TowFlight) = {
    this(launch0)
    copyFrom(f)
  }

  @OneToOne(fetch=FetchType.EAGER, mappedBy="towFlight", cascade = Array(CascadeType.ALL))
  var towLaunch : TowLaunch = _
  
  override def departureDate = towLaunch.flight.departureDate
  override def departureTime = towLaunch.flight.departureTime

  override def departureDate_=(d : Date) = { /*ignore*/ }
  override def departureTime_=(i : Int) = { /*ignore*/ }

  override def durationString = if(duration >= 0) String.format("%d:%02d", (duration / 60).asInstanceOf[AnyRef], (duration % 60).asInstanceOf[AnyRef]) else ""
  override protected def pUpdate(o:JsObject) = {} //tow flights are not syncedDown  
  
  override def jsonValues = {
    val s = super.jsonValues
    s + (JsString("controller_id") -> idToJson(towLaunch.flight.controller)) +
        (JsString("from_id") -> idToJson(towLaunch.flight.from)) +
        (JsString("departure_date") -> JsString(dt(departureDate).toString("yyyy-MM-dd")))
  }
}


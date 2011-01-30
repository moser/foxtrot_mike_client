package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.{JsString, JsValue}
import de.moserei.foxtrotmike.client.models.repos.AllFlights

@Entity
@DiscriminatorValue("F")
class Flight extends AbstractFlight {
  addObserver(AllFlights)

  @OneToOne(fetch=FetchType.EAGER, mappedBy="flight", cascade = Array(CascadeType.ALL))
  var launch : Launch = _
  
  def this(d : Defaults) {
    this()
    from = d.airfield
    to = d.airfield
    controller = d.controller
    departureDate = d.date
  }
  
  def launchType = {
    if(launch.isInstanceOf[WireLaunch]) {
      "WireLaunch"
    } else if(launch.isInstanceOf[TowLaunch]) {
      "TowLaunch"
    } else {
      "SelfLaunch"
    }
  }
  
  def launchType_=(s:String) = {
    if(launchType != s) {
      if(s == "SelfLaunch") {
        launch = null
      } else {
        if(s == "WireLaunch") {
          launch = new WireLaunch(this, DefaultsSingleton)
        } else {
          launch = new TowLaunch(this, DefaultsSingleton)
        }
      }
    }
  }
  
  def finished = {
    departureTime >= 0 && arrivalTime >= 0
  }
  
  override def jsonValues = {
    if(launch != null)
      super.jsonValues ++ Map[JsString, JsValue](JsString("launch_attributes") -> launch.toJson)
    else
      super.jsonValues
  }
}

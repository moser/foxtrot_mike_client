package fmclient.models

import javax.persistence._
import dispatch.json.{JsString, JsValue}
import fmclient.models.repos.AllFlights

@Entity
@DiscriminatorValue("F")
class Flight extends AbstractFlight {
  addObserver(AllFlights)

  @OneToOne(fetch=FetchType.EAGER, mappedBy="flight", cascade = Array(CascadeType.ALL))
  var launch : Launch = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="cost_hint_id")
  var costHint : CostHint  = _

  def this(d : Defaults) = {
    this()
    from = d.airfield
    to = d.airfield
    controller = d.controller
    departureDate = d.date
  }

  def this(f : Flight) = {
    this()
    copyFrom(f)
    f.launchType match {
      case "WireLaunch" => {
        launch = new WireLaunch(this, f.launch.asInstanceOf[WireLaunch])
      }
      case "TowLaunch" => {
        launch = new TowLaunch(this, f.launch.asInstanceOf[TowLaunch])
      }
      case _ => {}
    }
  }

  def launchType = {
    if(launch.isInstanceOf[WireLaunch]) {
      "wire_launch"
    } else if(launch.isInstanceOf[TowLaunch]) {
      "tow_launch"
    } else {
      "self_launch"
    }
  }

  def launchType_=(s:String) = {
    if(launchType != s) {
      if(s == "self_launch") {
        launch = null
      } else {
        if(s == "wire_launch") {
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
    val json = super.jsonValues ++ Map(JsString("cost_hint_id") -> idToJsonInt(costHint))
    if(launch != null)
      json ++ Map[JsString, JsValue](JsString("launch_attributes") -> launch.toJson)
    else
      json
  }
}

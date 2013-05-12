package fmclient.models

import javax.persistence._
import org.eclipse.persistence.annotations.PrivateOwned
import dispatch.json.{JsString, JsValue, JsArray}
import fmclient.models.repos.AllFlights
import scala.collection.mutable.ArraySeq
import scala.collection.immutable.List
import org.joda.time.{Days, DateMidnight}
import java.util.Date

@Entity
@DiscriminatorValue("F")
class Flight extends AbstractFlight {
  addObserver(AllFlights)

  @OneToOne(fetch=FetchType.EAGER, mappedBy="flight", cascade = Array(CascadeType.ALL))
  @PrivateOwned
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
      case "wire_launch" => {
        launch = new WireLaunch(this, f.launch.asInstanceOf[WireLaunch])
      }
      case "tow_launch" => {
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
      if(s == "self_launch" || s == "self") {
        if(launch != null)
          launch.delete
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

  @Lob
  var liabilities = ArraySeq[Liability]()

  def proportionFor(fl : Liability) = {
    fl.proportion.toFloat / liabilities.map(l => l.proportion.toFloat).sum
  }

  override def jsonValues = {
    val json = super.jsonValues ++ Map(JsString("cost_hint_id") -> idToJsonInt(costHint)) ++
                                   Map(JsString("liabilities_attributes") -> JsArray(List.concat(liabilities.map(e => e.toJson))))
    if(launch != null)
      json ++ Map[JsString, JsValue](JsString("launch_attributes") -> launch.toJson)
    else
      json
  }

  override def problematicFields = {
    var r = List[String]()
    if(launchTypeHasProblems) { r = r :+ "launchType" }
    if(departureDateHasProblems) { r = r :+ "departureDate" }
    r
  }

  def launchTypeHasProblems = {
    plane != null && plane.possibleLaunchMethods.indexOf(launchType) == -1
  }

  def departureDateHasProblems = {
    val diff = Days.daysBetween(new DateMidnight(departureDate), new DateMidnight(new Date())).getDays() 
    departureDate != null && (diff > 10 || diff < 0)
  }

  override def toString = {
    var r  = "" 
    if(plane != null)
      r += plane.toString + " "
    if(seat1 != null)
      r += seat1.toString + " "
    if(departureTime >= 0) 
      r += String.format("%d:%02d", (departureTime / 60).asInstanceOf[AnyRef], (departureTime % 60).asInstanceOf[AnyRef]) + " "
    if(arrivalTime >= 0) 
      r += String.format("%d:%02d", (arrivalTime / 60).asInstanceOf[AnyRef], (arrivalTime % 60).asInstanceOf[AnyRef])
    r
  }
}

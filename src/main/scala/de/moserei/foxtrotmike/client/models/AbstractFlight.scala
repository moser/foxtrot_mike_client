package de.moserei.foxtrotmike.client.models

import javax.persistence._
import org.joda.time.DateTime
import java.util.Date
import dispatch.json.{JsObject, JsString, JsNumber, JsValue}

@Entity
@Inheritance
abstract class AbstractFlight extends BaseModel[String] with UUIDHelper {
  @Id
  var id : String = createUUID

  @Temporal(TemporalType.TIMESTAMP)
  var pDepartureDate : Date = (new DateTime).toDateMidnight.toDate
  var pDepartureTime : Int = -1
  var pArrivalTime = -1

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="plane_id")
  var plane : Plane = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="seat1_id")
  var seat1 : Person = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="seat2_id")
  var seat2 : Person = _
  
  var seat2_number : Integer = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="from_id")
  var from : Airfield = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="to_id")
  var to : Airfield = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="controller_id")
  var controller : Person = _
  
  var status = "local"

  protected def dt(d:Date) = new DateTime(d)

  def departureDate = pDepartureDate
  def departureDate_=(d : Date) {
    pDepartureDate = dt(d).toDateMidnight.toDateTime.toDate
  }
  
  def departureTime = pDepartureTime
  def departureTime_=(i : Int) {
    pDepartureTime = i % 1440
  }
  
  def arrivalTime = pArrivalTime
  def arrivalTime_=(i : Int) {
    pArrivalTime = i % 1440
  }

  def duration = {
    if(departureTime < 0 || arrivalTime < 0)
      -1
    else {
      var delta = (arrivalTime - departureTime) % 1440
      if(delta >= 0) { delta } else { 1440 + delta }
    }
  }

  def durationString = if(duration >= 0) String.format("%d:%02d", (duration / 60).asInstanceOf[AnyRef], (duration % 60).asInstanceOf[AnyRef]) else ""
  override protected def pUpdate(o:JsObject) = {} //flights are not syncedDown
  
  override def jsonValues : Map[JsString, JsValue] = {
    Map(JsString("plane_id") -> idToJson(plane),
        JsString("seat1_id") -> idToJson(seat1),
        JsString("seat2_id") -> idToJson(seat2),
        JsString("from_id") -> idToJson(from),
        JsString("to_id") -> idToJson(to),
        JsString("controller_id") -> idToJson(controller),
        JsString("arrival_time") -> JsNumber(arrivalTime),
        JsString("departure_time") -> JsNumber(departureTime),
        JsString("departure_date") -> JsString(dt(departureDate).toString("yyyy-MM-dd")))
  }
  
  override def isValid = {
    invalidFields.isEmpty
  }
  
  override def invalidFields = {
    //TODO remove departureTime >+= 0 when implemented on the server
    var r = List[String]()
    if(!isPlaneValid) { r = r :+ "plane" }
    if(!isSeat1Valid) { r = r :+ "seat1" }
    if(!isFromValid) { r = r :+ "from" }
    if(!isToValid) { r = r :+ "to" }
    if(!isDepartureTimeValid) { r = r :+ "departureTime" }
    if(!isControllerValid) { r = r :+ "controller" }
    r
  }
  
  def isPlaneValid = plane != null
  def isSeat1Valid = seat1 != null
  def isFromValid = from != null
  def isToValid = to != null
  def isDepartureTimeValid = departureTime >= 0
  def isControllerValid = controller != null
  
  protected def copyFrom(f : AbstractFlight) = {
    departureDate = f.departureDate
    plane = f.plane
    seat1 = f.seat1
    seat2 = f.seat2
    from = f.from
    to = f.to
    controller = f.controller
  }
}

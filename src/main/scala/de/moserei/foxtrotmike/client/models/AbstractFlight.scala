package de.moserei.foxtrotmike.client.models

import javax.persistence._
import org.joda.time.DateTime
import java.util.Date
import dispatch.json.JsObject

@Entity
@Inheritance
abstract class AbstractFlight extends BaseModel with UUIDHelper {
  @Id
  var id : String = createUUID

  @Temporal(TemporalType.TIMESTAMP)
  var pDepartureDate : Date = (new DateTime).toDateMidnight.toDate
  var pDepartureTime = -1
  var duration : Int = -1

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="plane_id")
  var plane : Plane = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="seat1_id")
  var seat1 : Person = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="seat2_id")
  var seat2 : Person = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="from_id")
  var from : Airfield = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="to_id")
  var to : Airfield = _

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="controller_id")
  var controller : Person = _

  private def dt(d:Date) = new DateTime(d)

  def departureDate = pDepartureDate
  def departureTime = pDepartureTime

  def departureDate_=(d : Date) {
    pDepartureDate = dt(d).toDateMidnight.toDateTime.toDate
  }

  def departureTime_=(i : Int) {
    if(duration > 0) {
      val delta =  pDepartureTime - i
      duration += delta
    }
    pDepartureTime = i
  }

  def arrivalTime = if(duration >= 0) pDepartureTime + duration else -1
  def arrivalTime_=(i : Int) = {
    if(i >= 0) {
      duration = i - pDepartureTime
    } else {
      duration = -1
    }
    if(duration < 0) duration = -1
  }

  def durationString = if(duration >= 0) String.format("%d:%02d", (duration / 60).asInstanceOf[AnyRef], (duration % 60).asInstanceOf[AnyRef]) else ""
  override protected def pUpdate(o:JsObject) = {} //flights are not syncedDown
}
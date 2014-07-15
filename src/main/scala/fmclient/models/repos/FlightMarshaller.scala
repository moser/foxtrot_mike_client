package fmclient.models.repos
import net.liftweb.json._
import fmclient.models._
import net.liftweb.json.JsonDSL._
import org.joda.time.DateTime

object FlightMarshaller extends Marshaller[Flight] {
  implicit val formats = DefaultFormats

  def marshal(o : Flight) = {
    var values = (
      flightToJson(o) ~
      ("cost_hint_id" -> idToJson(o.costHint)) ~
      ("liabilities_attributes" ->
        o.liabilities.map((l) => ("person_id" -> l.personId) ~
                                 ("proportion" -> l.proportion)))
    )
    if(o.launch.isInstanceOf[TowLaunch]) {
      val l = o.launch.asInstanceOf[TowLaunch]
      values = values ~ (
        "launch_attributes" -> (flightToJson(l.towFlight) ~ ("type" -> "TowFlight"))
      )
    } else if(o.launch.isInstanceOf[WireLaunch]) {
      val l = o.launch.asInstanceOf[WireLaunch]
      values = values ~ (
        "launch_attributes" -> (
          ("type" -> "WireLaunch") ~
          ("wire_launcher_id" -> l.wireLauncher.id) ~
          ("operator_id" -> l.operator.id)
        )
      )
    }
    values
  }

  private def flightToJson(o : AbstractFlight) = {
    (
      ("id" -> o.id) ~
      ("plane_id" -> idToJson(o.plane)) ~
      ("seat1_person_id" -> idToJson(o.seat1)) ~
      ("seat2_person_id" -> idToJson(o.seat2)) ~
      ("seat2_n" -> o.seat2Number.toInt) ~
      ("from_id" -> idToJson(o.from)) ~
      ("to_id" -> idToJson(o.to)) ~
      ("controller_id" -> idToJson(o.controller)) ~
      ("engine_duration" -> o.engineDuration) ~
      ("arrival_i" -> o.arrivalTime) ~
      ("departure_i" -> o.departureTime) ~
      ("departure_date" -> new DateTime(o.departureDate).toString("yyyy-MM-dd")) ~
      ("comment" -> o.comment)
    )
  }
  
  private def idToJson[T](a : BaseModel[T]) = {
    if(a == null)
      None
    else
      Some(a.id)
  }

  def update(local : Flight, remote : Flight) {
    throw new Exception("Do not unmarshal a flight from remote.")
  }

  def unmarshalJson(value: JValue) = {
    throw new Exception("Do not unmarshal a flight from remote.")
    new Flight()
  }
}

// vim: set ts=2 sw=2 et:

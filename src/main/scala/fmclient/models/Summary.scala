package fmclient.models

import scala.collection.immutable.HashMap

import swing.Publisher
import swing.event.Event

import net.liftweb.json._
import scalaj.http._

case class SummaryReceived(counts : Map[String, BigInt]) extends Event

class Summary extends Publisher {
  def get {
    val json = Http(s"http://${Config.server}:${Config.port}/flights_summary")
               .option(HttpOptions.connTimeout(10000))
               .option(HttpOptions.readTimeout(50000))
               .asString
    
    val pjson = parse(json)
    var counts = new HashMap[String, BigInt]()
    pjson match {
      case JObject(fields) => {
        fields.foreach { field =>
          field match {
            case JField(name, JInt(value)) => counts = counts + (name -> value)
            case _ => throw new Exception("Unexpected JSON format")
          }
        }
      }
      case _ => throw new Exception("Expected js obj")
    }
    publish(SummaryReceived(counts))
  }
}

package fmclient.models

import javax.persistence._
import dispatch.json.{JsObject, JsString}

@Entity
@DiscriminatorValue("W")
class WireLaunch extends Launch {
  def this(flight0 : Flight) = {
    this()
    flight = flight0
  }
  
  def this(flight0 : Flight, d : Defaults) = {
    this(flight0)
    wireLauncher = d.wireLauncher
    operator = d.operator
  }
  
  def this(flight0 : Flight, l : WireLaunch) = {
    this(flight0)
    wireLauncher = l.wireLauncher
    operator = l.operator
  }
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="wire_launcher_id")
  var wireLauncher : WireLauncher = _
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="operator_id")
  var operator : Person = _
  
  override def jsonValues = {
    Map(JsString("type") -> JsString("WireLaunch"),
        JsString("wire_launcher_id") -> idToJson(wireLauncher))
        //JsString("operator_id") -> idToJson(operator)) //TODO implement on server side
  }
  
  override def toJson : JsObject = {
    JsObject(jsonValues);
  }

}

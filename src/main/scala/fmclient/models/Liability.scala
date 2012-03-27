package fmclient.models

import dispatch.json._

class Liability(var personId : String, var proportion : Int) extends Serializable {
  def toJson = {
    JsObject(Map(JsString("person_id") -> JsString(personId),
                 JsString("proportion") -> JsNumber(proportion)));
  }
}

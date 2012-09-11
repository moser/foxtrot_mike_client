package fmclient.models

import javax.persistence._
import dispatch.json.JsObject
import fmclient.models.repos.AllAttributes

@Entity
class Attribute extends BaseModel[String] {
  //addObserver(AllAttributes)
  
  def this(_id : String, _value : String) = {
    this()
    id = _id
    value = _value
  }

  @Id
  var id : String = ""
  var value = ""
  var status = "local"

  override def toString = id + ": " + value

  override protected def pUpdate(o:JsObject) = {}

  override def jsonValues = {
    Map()
  }

}

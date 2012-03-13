package fmclient.models

import javax.persistence._
import dispatch.json.{JsObject, JsString}
import dispatch.json.Js._
import fmclient.models.repos.AllLegalPlaneClasses

@Entity
class LegalPlaneClass extends BaseModel[Int] {
  addObserver(AllLegalPlaneClasses)

  @Id
  var id : Int = _

  var name : String = ""
  var status = "local"

  def this(o:JsObject) = {
    this()
    id = ('id ! num)(o).intValue
    update(o)
  }

  override protected def pUpdate(o:JsObject) = {
    name = ('name ?? str)(o).orNull
  }

  override def toString = {
    name
  }

  override def jsonValues = {
    Map(JsString("name") -> stringToJson(name))
  }
}

package fmclient.models

import javax.persistence._
import dispatch.json.{JsObject, JsString}
import dispatch.json.Js._
import fmclient.models.repos.AllGroups

@Entity
@Table(name="groups")
class Group extends BaseModel[Int] with UUIDHelper {
  addObserver(AllGroups)

  @Id
  var id : Int = _

  var name = ""
  var status = "local"

  def this(o:JsObject) = {
    this()
    id = ('id ! num)(o).intValue
    update(o)
  }

  override protected def pUpdate(o:JsObject) = {
    name = ('name ! str)(o)
  }
  
  override def jsonValues = {
    Map(JsString("name") -> JsString(name))
  }
  
  override def toString = name
}

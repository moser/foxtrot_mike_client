package fmclient.models

import javax.persistence._
import dispatch.json.{JsObject, JsString}
import dispatch.json.Js._
import fmclient.models.repos.{AllPeople, AllGroups}

@Entity
class Person extends BaseModel[String] with UUIDHelper {
  addObserver(AllPeople)

  @Id
  var id : String = createUUID

  var firstname = ""
  var lastname = ""
  var status = "local"
  var disabled = false

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="group_id")
  var group : Group = _

  def name = lastname + " " + firstname
  override def toString = name

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override protected def pUpdate(o:JsObject) = {
    firstname = ('firstname ! str)(o)
    lastname = ('lastname ! str)(o)
    group = AllGroups.find(('group_id ! num)(o).intValue)
    disabled = ('disabled ! bool)(o)
  }

  override def jsonValues = {
    Map(JsString("firstname") -> JsString(firstname),
        JsString("lastname") -> JsString(lastname),
        JsString("group_id") -> idToJsonInt(group))
  }
}

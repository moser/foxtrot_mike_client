package de.moserei.foxtrotmike.client.models

import javax.persistence._
import dispatch.json.JsObject
import dispatch.json.Js._

@Entity
class Person extends BaseModel with UUIDHelper {
  @Id
  var id : String = createUUID

  var firstname : String = ""
  var lastname : String = ""

  def name = firstname + " " + lastname
  override def toString = name

  def this(o:JsObject) = {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  override def update(o:JsObject) = {
    firstname = ('firstname ! str)(o)
    lastname = ('lastname ! str)(o)
  }
}

package specs.models

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import dispatch.json.{JsObject, JsString, JsNumber}

import dispatch.json.Js._
import de.moserei.foxtrotmike.client.models.BaseModel

class BaseModelSpec extends Spec with ShouldMatchers {
  class ConcreteModel extends BaseModel {
    var id = ""
    var name = ""
    var foo = 1

    def this(o:JsObject) {
      this()
      id = ('id ! str)(o)
      update(o)
    }

    override def update(o:JsObject) = {
      name = ('name ! str)(o)
      foo = ('foo ! num)(o).toInt
    }
  }

  val o = new JsObject(Map(JsString("id") -> JsString("123.1"), JsString("name") -> JsString("lala"), JsString("foo") -> JsNumber(2)))

  describe("BaseModel") {
    it("should update it's attributes from a JsObject") {
      val m = new ConcreteModel
      m.update(o)
      m.name should equal ("lala")
      m.foo should equal (2)
    }

    it("should set the id when constructed from a JsObject") {
      val m = new ConcreteModel(o)
      m.id should equal ("123.1")
    }
  }
}
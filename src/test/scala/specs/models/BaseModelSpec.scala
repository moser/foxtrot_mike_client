package specs.models

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import dispatch.json.{JsObject, JsString, JsNumber, JsValue}

import dispatch.json.Js._
import fmclient.models.BaseModel

class BaseModelSpec extends Spec with ShouldMatchers {
  class ConcreteModel extends BaseModel[String] {
    var id = ""
    var name = ""
    var foo = 1
    var status = "local"

    def this(o:JsObject) {
      this()
      id = ('id ! str)(o)
      update(o)
    }

    override protected def pUpdate(o:JsObject) = {
      name = ('name ! str)(o)
      foo = ('foo ! num)(o).toInt
    }
    
    override def jsonValues = {
      Map[JsString, JsValue]()
    }
  }

  val o = new JsObject(Map(JsString("id") -> JsString("123.1"), JsString("name") -> JsString("lala"), JsString("foo") -> JsNumber(2)))

  describe("BaseModel") {
    it("should be of local origin when created") {
      val m = new ConcreteModel
      m.status should equal ("local")
    }

    it("should update it's attributes from a JsObject") {
      val m = new ConcreteModel
      m.update(o)
      m.name should equal ("lala")
      m.foo should equal (2)
    }

    it("should update it's origin") {
      val m = new ConcreteModel
      m.update(o)
      m.status should equal ("remote")
    }

    it("should set the id when constructed from a JsObject") {
      val m = new ConcreteModel(o)
      m.id should equal ("123.1")
    }
  }
}

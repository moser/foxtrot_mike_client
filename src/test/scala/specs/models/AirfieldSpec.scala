package specs.models

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import dispatch.json.{JsObject, JsString, JsNull}
import de.moserei.foxtrotmike.client.models.Airfield

class AirfieldSpec extends Spec with ShouldMatchers {
  describe("airfield") {
    describe("constructor") {
      it("should accept a JsObject") {
        val o = new JsObject(Map(JsString("id") -> JsString("123.1"), JsString("registration") -> JsString("lala"), JsString("name") -> JsString("salf")))
        val a = new Airfield(o)
        a.id should equal ("123.1")
        a.registration should equal ("lala")
        a.name should equal ("salf")
      }

      it("should tolerate nulls") {
        val o = new JsObject(Map(JsString("id") -> JsString("123.1"), JsString("registration") -> JsNull, JsString("name") -> JsNull))
        val a = new Airfield(o)
        a.id should equal ("123.1")
        a.registration should equal (null)
        a.name should equal (null)
      }
    }
  }
}

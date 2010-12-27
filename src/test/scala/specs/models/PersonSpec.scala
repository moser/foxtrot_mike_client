package specs.models

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import dispatch.json.{JsObject, JsString}
import de.moserei.foxtrotmike.client.models.Person

class PersonSpec extends Spec with ShouldMatchers {
  describe("person") {
    it("should have a constructor accepting a JsObject") {
      val o = new JsObject(Map(JsString("id") -> JsString("123.1"), JsString("firstname") -> JsString("lala"), JsString("lastname") -> JsString("DDD")))
      val p = new Person(o)
      p.id should equal ("123.1")
      p.firstname should equal ("lala")
      p.lastname should equal ("DDD")
    }
  }
}
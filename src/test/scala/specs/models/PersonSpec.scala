package specs.models

import org.scalatest.{ Spec, BeforeAndAfterAll }
import org.scalatest.matchers.ShouldMatchers
import dispatch.json.{ JsObject, JsString, JsNumber }
import fmclient.models.{ Person, EntityMgr }

class PersonSpec extends Spec with ShouldMatchers with BeforeAndAfterAll {
  
  override def beforeAll(configMap: Map[String, Any]) {
    EntityMgr.init(true)
  }

  describe("person") {
    it("should have a constructor accepting a JsObject") {
      val o = new JsObject(Map(JsString("id") -> JsString("123.1"), JsString("firstname") -> JsString("lala"), JsString("lastname") -> JsString("DDD"), JsString("group_id") -> JsNumber(0)))
      val p = new Person(o)
      p.id should equal ("123.1")
      p.firstname should equal ("lala")
      p.lastname should equal ("DDD")
    }
  }
}

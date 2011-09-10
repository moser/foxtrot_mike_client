package specs.models

import org.scalatest.{ Spec, BeforeAndAfterAll }
import org.scalatest.matchers.ShouldMatchers
import dispatch.json.{JsObject, JsString, JsFalse, JsNumber}
import fmclient.models.{ Plane, EntityMgr }

class PlaneSpec extends Spec with ShouldMatchers with BeforeAndAfterAll {
  
  override def beforeAll(configMap: Map[String, Any]) {
    EntityMgr.init(true)
  }
  
  describe("plane") {
    it("should have a constructor accepting a JsObject") {
      val o = new JsObject(Map(JsString("id") -> JsString("123.1"), 
                                JsString("registration") -> JsString("lala"), 
                                JsString("make") -> JsString("some plane"),
                                //JsString("default_launch_method") -> JsString("t"), //??
                                JsString("has_engine") -> JsFalse,
                                JsString("can_fly_without_engine") -> JsFalse,
                                JsString("can_tow") -> JsFalse,
                                JsString("can_be_towed") -> JsFalse,
                                JsString("can_be_wire_launched") -> JsFalse,
                                JsString("disabled") -> JsFalse, 
                                JsString("group_id") -> JsNumber(0)))
      val p = new Plane(o)
      p.id should equal ("123.1")
      p.registration should equal ("lala")
      p.make should equal ("some plane")
    }
  }
}

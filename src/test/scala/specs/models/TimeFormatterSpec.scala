package specs.models

import org.scalatest.matchers.ShouldMatchers
import fmclient.models.LessSuckingTimeFormatter
import org.scalatest.{BeforeAndAfterEach, FunSpec}

class TimeFormatterSpec extends FunSpec with ShouldMatchers with BeforeAndAfterEach {
  var tf : LessSuckingTimeFormatter = _
  override def beforeEach() {
    tf = new LessSuckingTimeFormatter
  }

  describe("TimeFormatter") {
    describe("#stringToValue") {
      it("should try its best to generate a value") {
        var d = tf.stringToValue("11:12").asInstanceOf[Int]
        d should equal (672)
        d = tf.stringToValue("1:55").asInstanceOf[Int]
        d should equal (115)
        d = tf.stringToValue("1:66").asInstanceOf[Int]
        d should equal (66) //1:06
        d = tf.stringToValue("26:39").asInstanceOf[Int]
        d should equal (159) //2:39
        d = tf.stringToValue("11.12").asInstanceOf[Int]
        d should equal (672)
        d = tf.stringToValue("1.55").asInstanceOf[Int]
        d should equal (115)
        d = tf.stringToValue("1.66").asInstanceOf[Int]
        d should equal (66) //1:06
        d = tf.stringToValue("26.39").asInstanceOf[Int]
        d should equal (159) //2:39
        d = tf.stringToValue("1112").asInstanceOf[Int]
        d should equal (672)
        d = tf.stringToValue("155").asInstanceOf[Int]
        d should equal (115)
        d = tf.stringToValue("166").asInstanceOf[Int]
        d should equal (66) //1:06
        d = tf.stringToValue("2639").asInstanceOf[Int]
        d should equal (159) //2:39
        tf.stringToValue("--:--") should equal (-1)
      }
    }
    describe("#valueToString") {
      it("should generate a String") {
        tf.valueToString((-1).asInstanceOf[AnyRef]) should equal ("")
        tf.valueToString(689.asInstanceOf[AnyRef]) should equal ("11:29")
        tf.valueToString(98.asInstanceOf[AnyRef]) should equal ("01:38")
      }
    }
  }
}

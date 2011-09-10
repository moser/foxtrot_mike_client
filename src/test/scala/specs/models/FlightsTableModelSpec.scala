package specs.models

import org.scalatest.{ Spec, BeforeAndAfterAll }
import org.scalatest.matchers.ShouldMatchers
import de.moserei.foxtrotmike.client.models.{ FlightsTableModel, Flight, EntityMgr }

class FlightsTableModelSpec extends Spec with ShouldMatchers with BeforeAndAfterAll {

  override def beforeAll(configMap: Map[String, Any]) {
    EntityMgr.init(true)
  }

  describe("A flight table model") {
    describe("filters") {
      it("should only return unfinished flights") {
        val m = new FlightsTableModel
        val f = new Flight
        //TODO stub the DB or create an own DB for test
        f.save
        m.getAll.length should be > 0
        f.departureTime = 0
        f.arrivalTime = 10
        f.save
        m.getAll.length should be > 0
        m.unfinishedOnly = true
        m.getAll.length should equal(0)
      }
    }
  }
}

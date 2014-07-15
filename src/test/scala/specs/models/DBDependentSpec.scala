// vim: set ts=2 sw=2 et:
package test.scala.specs.models

import org.scalatest.{ FunSpec, BeforeAndAfterAll }
import org.scalatest.matchers.ShouldMatchers
import fmclient.models.{ FlightsTableModel, Flight, EntityMgr }
import fmclient.models.repos.AllFlights

class DbDependentSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  override def beforeAll() {
    EntityMgr.init(true)
  }

  describe("A flight table model") {
    describe("filters") {
      it("should only return unfinished flights") {
        AllFlights.all.map(_.delete)
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

  override def afterAll() {
    EntityMgr.close
  }
}

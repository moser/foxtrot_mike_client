package specs.models

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import fmclient.models.{Airfield, Flight, Person, Plane}
import org.joda.time.DateTime

class FlightSpec extends FunSpec with ShouldMatchers {
  describe("A flight") {
    it("should copy some attributes from another flight") {
      val f0 = new Flight
      f0.departureDate = new DateTime(2010, 12, 13, 0, 0, 0, 0).toDate
      f0.plane = new Plane
      f0.seat1 = new Person
      f0.seat2 = new Person
      f0.from = new Airfield
      f0.to = f0.from
      f0.departureTime = 65 //1:05
      f0.arrivalTime = 70 //1:10
      f0.controller = new Person
      val f = new Flight(f0)
      f.departureDate should equal (f0.departureDate)
      f.plane should equal (f0.plane)
      f.seat1 should equal (f0.seat1)
      f.seat2 should equal (f0.seat2)
      f.from should equal (f0.from)
      f.to should equal (f0.to)
      f.departureTime should equal (-1)
      f.arrivalTime should equal (-1)
      f.controller should equal (f0.controller)
    }

    describe("departureTime") {
      it("should not allow values gteq 1440") {
        val f = new Flight
        f.departureTime = 1441
        f.departureTime should equal (1)
      }
    }

    describe("arrivalTime") {
      it("should not allow values gteq 1440") {
        val f = new Flight
        f.arrivalTime = 1441
        f.arrivalTime should equal (1)
      }
    }

    describe("duration") {
      it("should return null when arrival is not set") {
        val f = new Flight
        f.departureTime = 10 //0:10
        f.duration should equal (-1)
      }

      it("should return the difference between departure and arrival") {
        val f = new Flight
        f.departureTime = 10 //0:10
        f.arrivalTime = 10 //0:10
        f.duration should equal (0)
        f.departureTime = 10 //0:10
        f.arrivalTime = 20 //0:20
        f.duration should equal (10)
        f.departureTime = 10 //0:10
        f.arrivalTime = 9 //0:09
        f.duration should equal (1439)
      }
    }

    describe("durationString") {
      it("should return a padded string") {
        val f = new Flight
        f.departureTime = 0
        f.arrivalTime = 1
        f.durationString should equal ("0:01")
        f.arrivalTime = 69
        f.durationString should equal ("1:09")
        f.arrivalTime = 90
        f.durationString should equal ("1:30")
        f.arrivalTime = -1
        f.durationString should equal ("")
      }
    }

    it("should tell if it is finished") {
      val f = new Flight
      f.finished should equal (false)
      f.departureTime = 65 //1:05
      f.finished should equal (false)
      f.arrivalTime = 70 //1:10
      f.finished should equal (true)
    }

    it("should only be valid if all the required attributes are set") {
      val f = new Flight
      f should not be ('valid)
      f.plane = new Plane
      f should not be ('valid)
      f.seat1 = new Person
      f should not be ('valid)
      f.departureTime = 10 //0:10
      f should not be ('valid)
      f.controller = new Person
      f should not be ('valid)
      f.from = new Airfield
      f should not be ('valid)
      f.to = new Airfield
      f should be ('valid)
    }
  }
}

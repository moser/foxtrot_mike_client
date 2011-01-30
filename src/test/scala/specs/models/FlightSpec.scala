package specs.models

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import de.moserei.foxtrotmike.client.models.{Airfield, Flight, Person, Plane}

class FlightSpec extends Spec with ShouldMatchers {
  describe("A flight") {
    describe("departureTime") {
      it("should change the duration if it is set") {
        val f = new Flight
        f.departureTime = 65 //1:05
        f.duration = 30
        f.departureTime = 75 //1:15
        f.duration should equal (20)
        f.departureTime = 60 //1:00
        f.duration should equal (35)
      }
    }

    describe("arrival Time") {
      it("should return null when duration is < 0") {
        val f = new Flight
        f.duration = -1
        f.arrivalTime should equal (-1)
      }

      it("should return a Date when duration is >= 0") {
        val f = new Flight
        f.pDepartureTime = 612 //10:12
        f.duration = 0
        f.arrivalTime should not equal (-1)
        f.duration = 10
        f.arrivalTime should equal (622) //10:22
      }

      it("should set the duration") {
        val f = new Flight
        f.departureTime = 0 //0:00
        f.arrivalTime = 65
        f.duration should equal (65)
      }

       it("should set the duration to -1 when passed null") {
        val f = new Flight
        f.departureTime = 0 //0:00
        f.duration = 120
        f.arrivalTime = -1
        f.duration should equal (-1)
      }
    }

    describe("durationString") {
      it("should return a padded string") {
        val f = new Flight
        f.duration = 1
        f.durationString should equal ("0:01")
        f.duration = 69
        f.durationString should equal ("1:09")
        f.duration = 90
        f.durationString should equal ("1:30")
        f.duration = -1
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

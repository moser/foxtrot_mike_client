package specs.models

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import de.moserei.foxtrotmike.client.models.{Airfield, Flight, Person, Plane}
import org.joda.time.DateTime

class FlightSpec extends Spec with ShouldMatchers {
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

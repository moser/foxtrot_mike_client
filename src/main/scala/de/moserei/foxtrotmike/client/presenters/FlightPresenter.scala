package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.FlightView
import de.moserei.foxtrotmike.client.models.Flight
import scala.swing.event._
import java.util.Date
import swing.Reactor



class FlightPresenter(view0: FlightView) extends BasePresenter[Flight, FlightView] {
  def this() = this(new FlightView())  
  var model : Flight = _
  var view = view0

  map((m,v) => m.plane = v.plane.selectedItem, (m,v) => v.plane.selectedItem = m.plane)
  map((m,v) => m.seat1 = v.seat1.selectedItem, (m,v) => v.seat1.selectedItem = m.seat1)
  map((m,v) => m.seat2 = v.seat2.selectedItem, (m,v) => v.seat2.selectedItem = m.seat2)
  map((m,v) => m.from = v.from.selectedItem, (m,v) => v.from.selectedItem = m.from)
  map((m,v) => m.to = v.to.selectedItem, (m,v) => v.to.selectedItem = m.to)
  map((m,v) => m.departureDate = v.departureDate.peer.getValue.asInstanceOf[Date], (m,v) => v.departureDate.peer.setValue(m.departureDate))
  map((m,v) => m.arrivalTime = v.arrivalTime.peer.getValue.asInstanceOf[Int], (m,v) => v.arrivalTime.peer.setValue(m.arrivalTime))
  map((m,v) => m.departureTime = v.departureTime.peer.getValue.asInstanceOf[Int], (m,v) => v.departureTime.peer.setValue(m.departureTime))
  map((m,v) => {}, (m,v) => v.duration.text = m.durationString)

  view.btSave.reactions += {
    case ButtonClicked(_) => {
      updateModel
      model.save
    }
  }

  val timeFocus = new Reactor {
    listenTo(view.departureTime, view.arrivalTime)
    reactions += {
      case FocusLost(_, _, _) => {
        updateModel
        updateView
      }
    }
  }
}

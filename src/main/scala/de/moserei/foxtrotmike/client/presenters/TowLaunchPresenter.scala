package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.TowLaunchView
import de.moserei.foxtrotmike.client.models.TowLaunch
import scala.swing.event._
import swing.Reactor


class TowLaunchPresenter(view0: TowLaunchView) extends BasePresenter[TowLaunch, TowLaunchView] {
  def this() = this(new TowLaunchView())  
  var model : TowLaunch = _
  var view = view0

  map((m,v) => m.towFlight.plane = v.plane.selectedItem, (m,v) => v.plane.selectedItem = m.towFlight.plane)
  map((m,v) => m.towFlight.seat1 = v.seat1.selectedItem, (m,v) => v.seat1.selectedItem = m.towFlight.seat1)
  map((m,v) => m.towFlight.to = v.to.selectedItem, (m,v) => v.to.selectedItem = m.towFlight.to)
  map((m,v) => m.towFlight.arrivalTime = v.arrivalTime.peer.getValue.asInstanceOf[Int], (m,v) => v.arrivalTime.peer.setValue(m.towFlight.arrivalTime))
  map((m,v) => {}, (m,v) => v.duration.text = m.towFlight.durationString)
  
  val timeFocus = new Reactor {
    listenTo(view.arrivalTime)
    reactions += {
      case FocusLost(_, _, _) => {
        updateModel
        updateView
      }
    }
  }
  
  val timeSetter = new TimeSetter(this) { 
    add(view.btArrivalTime, view.arrivalTime)
  }
}

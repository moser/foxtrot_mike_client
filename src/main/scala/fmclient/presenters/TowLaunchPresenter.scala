package fmclient.presenters

import fmclient.views.TowLaunchView
import fmclient.models.TowLaunch
import scala.swing.event._
import swing.Reactor


class TowLaunchPresenter(view0: TowLaunchView) extends BasePresenter[TowLaunch, TowLaunchView] {
  def this() = this(new TowLaunchView())  
  var model : TowLaunch = _
  var view = view0

  map((m,v) => m.towFlight.plane = v.plane.selectedOption.get, (m,v) => v.plane.selectedOption = m.towFlight.plane)
  map((m,v) => m.towFlight.seat1 = v.seat1.selectedOption.get, (m,v) => v.seat1.selectedOption = m.towFlight.seat1)
  map((m,v) => m.towFlight.to = v.to.selectedOption.get, (m,v) => v.to.selectedOption = m.towFlight.to)
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

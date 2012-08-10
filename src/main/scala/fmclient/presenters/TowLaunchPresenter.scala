package fmclient.presenters

import fmclient.views.TowLaunchView
import fmclient.models.TowLaunch
import scala.swing.event._
import swing.Reactor
import fmclient.views.AutoCompleter._


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
  
  view.plane.reactions += {
    case CreateEvent(c, str, old) => {
      view.plane.setEnabled(false)
      val bp = new PlaneBalloonPresenter(str, view.plane)
      bp.reactions += {
        case PlaneBalloonPresenter.OkEvent(o) => {
          view.plane.selectedOption = o
        }
        case PlaneBalloonPresenter.CancelEvent() => {
          view.plane.revertLast
        }
      }
    }
  }

  val seatCreate = new Reactor {
    listenTo(view.seat1)
    reactions += {
      case CreateEvent(c, str, old) => {
        c.setEnabled(false)
        val bp = new PersonBalloonPresenter(str, c)
        bp.reactions += {
          case PersonBalloonPresenter.OkEvent(o) => {
            c.selectedOption = o
          }
          case PersonBalloonPresenter.CancelEvent() => {
            c.revertLast
          }
        }
      }
    }
  }

  val airfieldCreate = new Reactor {
    listenTo(view.to)
    reactions += {
      case CreateEvent(c, str, _) => {
        val bp = new AirfieldBalloonPresenter(str, c)
        bp.reactions += {
          case AirfieldBalloonPresenter.OkEvent(o) => {
            c.selectedOption = o
          }
          case AirfieldBalloonPresenter.CancelEvent() => {
            c.revertLast
          }
        }
      }
    }
  }
}

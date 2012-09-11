package fmclient.presenters

import fmclient.views.WireLaunchView
import fmclient.models.WireLaunch
import scala.swing.event._
import java.util.Date
import swing.Reactor
import fmclient.views.AutoCompleter._

class WireLaunchPresenter(view0: WireLaunchView) extends BasePresenter[WireLaunch, WireLaunchView] {
  def this() = this(new WireLaunchView())  
  var model : WireLaunch = _
  var view = view0

  map((m,v) => m.wireLauncher = v.wireLauncher.selectedOption.get, (m,v) => v.wireLauncher.selectedOption = m.wireLauncher)
  map((m,v) => m.operator = v.operator.selectedOption.get, (m,v) => v.operator.selectedOption = m.operator)
/*
  view.wireLauncher.reactions += {
    case CreateEvent(c, str, old) => {
      view.wireLauncher.setEnabled(false)
      val bp = new WireLauncherBalloonPresenter(str, view.wireLauncher)
      bp.reactions += {
        case PlaneBalloonPresenter.OkEvent(o) => {
          view.wireLauncher.selectedOption = o
        }
        case PlaneBalloonPresenter.CancelEvent() => {
          view.wireLauncher.revertLast
        }
      }
    }
  }
*/
  val seatCreate = new Reactor {
    listenTo(view.operator)
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
}

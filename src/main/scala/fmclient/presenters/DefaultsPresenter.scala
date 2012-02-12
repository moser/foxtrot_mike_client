package fmclient.presenters

import fmclient.views.DefaultsView
import fmclient.models.{DefaultsSingleton, Defaults}
import swing.Reactor
import scala.swing.event._
import java.util.Date

class DefaultsPresenter(view0: DefaultsView) extends BasePresenter[Defaults, DefaultsView] {
  var model : Defaults = DefaultsSingleton
  var view = view0


  map((m,v) => m.towPlane = v.towPlane.selection.item, (m,v) => v.towPlane.selection.item = m.towPlane)
  map((m,v) => m.towPilot = v.towPilot.selection.item, (m,v) => v.towPilot.selection.item = m.towPilot)
  map((m,v) => m.controller = v.controller.selection.item, (m,v) => v.controller.selection.item = m.controller)
  map((m,v) => m.date = v.date.peer.getValue.asInstanceOf[Date], (m,v) => v.date.peer.setValue(m.date))
  map((m,v) => m.wireLauncher = v.wireLauncher.selection.item, (m,v) => v.wireLauncher.selection.item = m.wireLauncher)
  map((m,v) => m.operator = v.operator.selection.item, (m,v) => v.operator.selection.item = m.operator)
  map((m,v) => m.airfield = v.airfield.selection.item, (m,v) => v.airfield.selection.item = m.airfield)

  updateView

  val focus = new Reactor {
    listenTo(view.towPlane, view.towPilot, view.controller, view.date, view.wireLauncher, view.airfield, view.operator)
    reactions += {
      case FocusLost(_, _, _) => {
        if(view.date.peer.isEditValid) {
          view.date.peer.commitEdit
        }
        updateModel
        updateView
      }
    }
  }

  val update = new Reactor {
    listenTo(model)
    reactions += {
      case Defaults.Updated() => {
        updateView
        updateModel
      }
    }
  }
}

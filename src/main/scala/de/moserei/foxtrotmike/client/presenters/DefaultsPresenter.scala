package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.DefaultsView
import de.moserei.foxtrotmike.client.models.{DefaultsSingleton, Defaults}
import swing.Reactor
import scala.swing.event._
import java.util.Date

class DefaultsPresenter(view0: DefaultsView) extends BasePresenter[Defaults, DefaultsView] {
  var model : Defaults = DefaultsSingleton
  var view = view0


  map((m,v) => m.towPlane = v.towPlane.selection.item, (m,v) => v.towPlane.selection.item = m.towPlane)
  map((m,v) => m.controller = v.controller.selection.item, (m,v) => v.controller.selection.item = m.controller)
  map((m,v) => m.date = v.date.peer.getValue.asInstanceOf[Date], (m,v) => v.date.peer.setValue(m.date))
  map((m,v) => m.wireLauncher = v.wireLauncher.selection.item, (m,v) => v.wireLauncher.selection.item = m.wireLauncher)
  map((m,v) => m.airfield = v.airfield.selection.item, (m,v) => v.airfield.selection.item = m.airfield)

  updateView

  val focus = new Reactor {
    listenTo(view.towPlane, view.controller, view.date, view.wireLauncher, view.airfield)
    reactions += {
      case FocusLost(_, _, _) => {
        updateModel
        updateView
      }
    }
  }
}
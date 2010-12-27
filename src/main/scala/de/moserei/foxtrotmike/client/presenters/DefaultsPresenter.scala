package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.DefaultsView
import de.moserei.foxtrotmike.client.models.{DefaultsSingleton, Defaults}
import swing.Reactor
import scala.swing.event._

class DefaultsPresenter(view0: DefaultsView) extends BasePresenter[Defaults, DefaultsView] {
  var model : Defaults = DefaultsSingleton
  var view = view0

  map((m,v) => v.towPlane.selection.item = m.towPlane, (m,v) => m.towPlane = v.towPlane.selection.item)

  updateView

  val focus = new Reactor {
    listenTo(view.towPlane)
    reactions += {
      case FocusLost(_, _, _) => {
        updateModel
        updateView
      }
    }
  }
}
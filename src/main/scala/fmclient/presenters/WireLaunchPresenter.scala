package fmclient.presenters

import fmclient.views.WireLaunchView
import fmclient.models.WireLaunch
import scala.swing.event._
import java.util.Date
import swing.Reactor



class WireLaunchPresenter(view0: WireLaunchView) extends BasePresenter[WireLaunch, WireLaunchView] {
  def this() = this(new WireLaunchView())  
  var model : WireLaunch = _
  var view = view0

  map((m,v) => m.wireLauncher = v.wireLauncher.selectedItem, (m,v) => v.wireLauncher.selectedItem = m.wireLauncher)
  map((m,v) => m.operator = v.operator.selectedItem, (m,v) => v.operator.selectedItem = m.operator)
}

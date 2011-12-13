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

  map((m,v) => m.wireLauncher = v.wireLauncher.selectedOption.get, (m,v) => v.wireLauncher.selectedOption = m.wireLauncher)
  map((m,v) => m.operator = v.operator.selectedOption.get, (m,v) => v.operator.selectedOption = m.operator)
}

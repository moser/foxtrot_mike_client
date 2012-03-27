package fmclient.views

import swing._
import fmclient.models.repos._
import fmclient.models._
import fmclient.views.AutoCompleter.DefaultItemRenderer

class WireLaunchView extends MigPanel("ins 0, gap 4!, fill", "[gp 0][gp 1][gp 1][gp 1][gp 1][gp 0][gp 0][gp 0][gp 1]") {
  val wireLauncher = new AutoCompleter(new DefaultAutoCompleterModel[WireLauncher](AllWireLaunchers, _.registration, Map("allowNil" -> false)))
  val operator = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false)))

  peer.add(wireLauncher, "w 80::")
  peer.add(operator, "w 110::, sg names, grow")
  add(new Label(""), "sg names, grow")
  add(new Label(""), "w 70::, sg airfields, grow")
  add(new Label(""), "sg airfields, grow")
  add(new Label(""), "w 70::, sg times")
  add(new Label(""), "sg times")
  add(new Label(""), "w 60::, sg durations")
  add(new Label(""), "sg names, grow, wrap")

  var _enabled = true
  override def enabled = _enabled
  override def enabled_=(b:Boolean) = {
    _enabled = b
    wireLauncher.setEnabled(b)
    operator.setEnabled(b)
  }
}

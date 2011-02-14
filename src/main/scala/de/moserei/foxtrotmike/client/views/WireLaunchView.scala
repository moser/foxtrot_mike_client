package de.moserei.foxtrotmike.client.views

import swing._
import de.moserei.foxtrotmike.client.models.repos._
import de.moserei.foxtrotmike.client.models._
import de.moserei.foxtrotmike.client.views.AutoCompleter.DefaultItemRenderer

class WireLaunchView extends MigPanel("ins 0", "[80!]5[120!]") {
  val wireLauncher = new AutoCompleter(new DefaultAutoCompleterModel[WireLauncher](AllWireLaunchers, _.registration, Map("allowNil" -> false)))
  val operator = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false)))

  peer.add(wireLauncher, "w 80!")
  peer.add(operator, "w 120!")
  
  var _enabled = true
  override def enabled = _enabled
  override def enabled_=(b:Boolean) = {
    _enabled = b
    wireLauncher.setEnabled(b)
    operator.setEnabled(b)
  }
}

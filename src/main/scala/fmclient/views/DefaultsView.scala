package fmclient.views

import swing._
import java.text.DateFormat
import fmclient.models._
import fmclient.models.repos._

class DefaultsView extends MigPanel("") { 
  val date = new FormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT)) {
    focusLostBehavior = FormattedTextField.FocusLostBehavior.CommitOrRevert
  }
  val airfield = new MyComboBox[Airfield](new MyComboBoxModel(AllAirfields))
  val towPlane = new MyComboBox[Plane](new MyComboBoxModel(TowPlanes))
  val towPilot = new MyComboBox[Person](new MyComboBoxModel(AllPeople))
  val wireLauncher = new MyComboBox[WireLauncher](new MyComboBoxModel(AllWireLaunchers))
  val operator = new MyComboBox[Person](new MyComboBoxModel(AllPeople))
  val controller = new MyComboBox[Person](new MyComboBoxModel(AllPeople))

  add(new Label(I18n("departure_date")))
  add(new Label(I18n("airfield")))
  add(new Label(I18n("tow_plane")))
  add(new Label(I18n("tow_pilot")))
  add(new Label(I18n("wire_launcher")))
  add(new Label(I18n("operator")))
  add(new Label(I18n("controller")), "wrap")

  add(date, "w 70")
  add(airfield)
  add(towPlane)
  add(towPilot)
  add(wireLauncher)
  add(controller)
  add(operator)
}

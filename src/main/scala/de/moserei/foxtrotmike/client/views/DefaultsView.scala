package de.moserei.foxtrotmike.client.views

import swing._
import java.text.DateFormat
import javax.swing.{DefaultComboBoxModel, ComboBoxModel, JComboBox}
import scalaj.collection.Imports._
import de.moserei.foxtrotmike.client.models._
import de.moserei.foxtrotmike.client.models.repos._

class MyComboBox[T](model : ComboBoxModel) extends ComboBox[T](Nil) {
  override lazy val peer: JComboBox = new JComboBox(model) with SuperMixin
}

//TODO should depend on BaseEntityRepository#all (reload often, see DefaultAutoCompleterModel)
class MyComboBoxModel[T](a : Seq[T]) extends DefaultComboBoxModel(new java.util.Vector(a.asJava))

class DefaultsView extends MigPanel("") { 
  val date = new FormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT)) {
    focusLostBehavior = FormattedTextField.FocusLostBehavior.CommitOrRevert
  }
  val airfield = new MyComboBox[Airfield](new MyComboBoxModel(AllAirfields.all))
  val towPlane = new MyComboBox[Plane](new MyComboBoxModel(TowPlanes.all))
  val wireLauncher = new MyComboBox[WireLauncher](new MyComboBoxModel(AllWireLaunchers.all))
  val controller = new MyComboBox[Person](new MyComboBoxModel(AllPeople.all))
  val operator = new MyComboBox[Person](new MyComboBoxModel(AllPeople.all))
  
  add(new Label(I18n("departure_date")))
  add(new Label(I18n("airfield")))
  add(new Label(I18n("tow_plane")))
  add(new Label(I18n("wire_launcher")))
  add(new Label(I18n("controller")))
  add(new Label(I18n("operator")), "wrap")
  
  add(date, "w 70")
  add(airfield)
  add(towPlane)
  add(wireLauncher)
  add(controller)
  add(operator)
}

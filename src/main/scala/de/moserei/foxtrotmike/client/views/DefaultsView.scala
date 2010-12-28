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

class MyComboBoxModel[T](a : Seq[T]) extends DefaultComboBoxModel(new java.util.Vector(a.asJava))

class DefaultsView extends MigPanel("wrap 1") {
  add(new Label(I18n("defaults.date")))
  val date = new FormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT)) {
    focusLostBehavior = FormattedTextField.FocusLostBehavior.CommitOrRevert
  }
  add(date, "w 70")

  add(new Label(I18n("defaults.controller")))
  val controller = new MyComboBox[Person](new MyComboBoxModel(AllPeople.all))
  add(controller)

  add(new Label(I18n("defaults.airfield")))
  val airfield = new MyComboBox[Airfield](new MyComboBoxModel(AllAirfields.all))
  add(airfield)

  add(new Label(I18n("defaults.towPlane")))
  val towPlane = new MyComboBox[Plane](new MyComboBoxModel(AllPlanes.all))
  add(towPlane)
  add(new Label(I18n("defaults.wireLauncher")))
  val wireLauncher = new MyComboBox[WireLauncher](new MyComboBoxModel(AllWireLaunchers.all))
  add(wireLauncher)

}
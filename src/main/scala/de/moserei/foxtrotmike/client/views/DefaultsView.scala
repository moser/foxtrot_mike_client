package de.moserei.foxtrotmike.client.views

import swing._
import javax.swing.{DefaultComboBoxModel, ComboBoxModel, JComboBox}
import scalaj.collection.Imports._
import de.moserei.foxtrotmike.client.models.Plane
import de.moserei.foxtrotmike.client.models.repos.AllPlanes

class MyComboBox[T](model : ComboBoxModel) extends ComboBox[T](Nil) {
  override lazy val peer: JComboBox = new JComboBox(model) with SuperMixin
}

class MyComboBoxModel[T](a : Seq[T]) extends DefaultComboBoxModel(new java.util.Vector(a.asJava)) {

}


class DefaultsView extends MigPanel {
  val towPlane = new MyComboBox[Plane](new MyComboBoxModel(AllPlanes.all))
  add(towPlane)
}
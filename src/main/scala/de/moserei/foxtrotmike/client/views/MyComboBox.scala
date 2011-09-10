package de.moserei.foxtrotmike.client.views

import swing._
import javax.swing.{DefaultComboBoxModel, ComboBoxModel, JComboBox}

class MyComboBox[T](model : ComboBoxModel) extends ComboBox[T](Nil) {
  override lazy val peer: JComboBox = new JComboBox(model) with SuperMixin
}

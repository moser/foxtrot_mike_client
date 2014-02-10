package fmclient.views

import swing._
import javax.swing.{DefaultComboBoxModel, ComboBoxModel, JComboBox}

class MyComboBox[T](var model : ComboBoxModel[T]) extends ComboBox[T](Nil) {
  override lazy val peer: JComboBox[T] = new JComboBox[T](model) with SuperMixin
}

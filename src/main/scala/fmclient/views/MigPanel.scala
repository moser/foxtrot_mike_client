package fmclient.views

import swing._
import net.miginfocom.swing.MigLayout


class MigPanel(constraints: String, colConstraints: String, rowConstraints: String) extends Panel {
  def this(constraints: String, colConstraints: String) = {
    this(constraints, colConstraints, "")
  }
  
  def this(constraints: String) = {
    this(constraints, "", "")
  }
  
  def this() {
    this("", "", "")
  }
  
  override lazy val peer = new javax.swing.JPanel(new MigLayout(constraints, colConstraints, rowConstraints)) with SuperMixin

  def add(c: Component, l: String = "") {
    peer.add(c.peer, l)
  }
  
  def remove(c: Component) {
    peer.remove(c.peer)
  }
}

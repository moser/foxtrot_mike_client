package de.moserei.foxtrotmike.client.views

import swing._
import net.miginfocom.swing.MigLayout


class MigPanel(constraint: String) extends Panel {
  override lazy val peer = new javax.swing.JPanel(new MigLayout(constraint)) with SuperMixin

  def this() {
    this("")
  }

  def add(c: Component, l: String = "") {
    peer.add(c.peer, l)
  }

}

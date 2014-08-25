package fmclient.views

import fmclient.models.{ I18n }
import swing._
import javax.swing.JComponent
import net.miginfocom.swing.MigLayout

class AirfieldBalloonView(attach : JComponent) extends BalloonView(attach) {
  val name = new TextField
  val registration = new TextField
  val btOk = new Button(I18n("ok"))
  val btCancel = new Button(I18n("cancel"))
  setLayout(new MigLayout("fill, wrap 2"))
  add(new Label(I18n("airfield_balloon.title")).peer, "span 2")
  add(new Label(I18n("name") + " *").peer)
  add(name.peer, "w 200")
  add(new Label(I18n("registration")).peer)
  add(registration.peer, "w 200")
  add(btOk.peer)
  add(btCancel.peer)
  init
  name.peer.requestFocusInWindow
}

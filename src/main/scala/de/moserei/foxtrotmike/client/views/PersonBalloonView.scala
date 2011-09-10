package de.moserei.foxtrotmike.client.views

import de.moserei.foxtrotmike.client.models.{ I18n, Group, MyComboBoxModel }
import de.moserei.foxtrotmike.client.models.repos.AllGroups
import swing._
import java.awt.Color
import net.java.balloontip.CustomFlexBalloonTip
import net.java.balloontip.FlexBalloonTip.{ Orientation, AttachLocation }
import net.java.balloontip.styles.RoundedBalloonStyle
import javax.swing.JComponent
import net.miginfocom.swing.MigLayout

class PersonBalloonView(attach : JComponent) extends BalloonView(attach) {
  val firstname = new TextField
  val lastname = new TextField
  val group = new MyComboBox[Group](new MyComboBoxModel(AllGroups))
  val btOk = new Button(I18n("ok"))
  val btCancel = new Button(I18n("cancel"))
  //val group = new MyComboBox[Group](new MyComboBoxModel(AllGroups))
  setLayout(new MigLayout("fill, wrap 2"))
  add(new Label(I18n("person_balloon.title")).peer, "span 2")
  add(new Label(I18n("firstname")).peer)
  add(firstname.peer, "w 200")
  add(new Label(I18n("lastname")).peer)
  add(lastname.peer, "w 200")
  add(new Label(I18n("group")).peer)
  add(group.peer, "w 150")
  add(btOk.peer)
  add(btCancel.peer)
  init
  firstname.peer.requestFocusInWindow
}

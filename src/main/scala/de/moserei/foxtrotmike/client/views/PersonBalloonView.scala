package de.moserei.foxtrotmike.client.views

import de.moserei.foxtrotmike.client.models.I18n
import swing._
import java.awt.Color
import net.java.balloontip.CustomFlexBalloonTip
import net.java.balloontip.FlexBalloonTip.{ Orientation, AttachLocation }
import net.java.balloontip.styles.MinimalBalloonStyle
import javax.swing.JComponent
import net.miginfocom.swing.MigLayout

class PersonBalloonView(val attach : JComponent) extends CustomFlexBalloonTip(attach, 
                                                                new MinimalBalloonStyle(Color.GRAY, 5),
                                                                Orientation.LEFT_BELOW,
                                                                AttachLocation.ALIGNED, 25, 10)  {
  val firstname = new TextField
  val lastname = new TextField
  val btOk = new Button("Ok")
  val btCancel = new Button("Cancel")
  //val group = new MyComboBox[Group](new MyComboBoxModel(AllGroups))
  setLayout(new MigLayout("fill, wrap 2"))
  add(new Label(I18n("firstname")).peer)
  add(firstname.peer, "w 100")
  add(new Label(I18n("lastname")).peer)
  add(lastname.peer, "w 100")
  //add(new Label(I18n("group")).peer)
  //add(group.peer, "w 100")
  add(btOk.peer)
  add(btCancel.peer)
  init
  firstname.peer.requestFocusInWindow
}

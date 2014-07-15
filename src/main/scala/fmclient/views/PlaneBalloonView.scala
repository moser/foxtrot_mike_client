package fmclient.views

import fmclient.models.{ I18n, Group, MyComboBoxModel, LegalPlaneClass, LaunchItem }
import fmclient.models.repos.{ AllGroups, AllLegalPlaneClasses }
import swing._
import java.awt.Color
import net.java.balloontip.CustomFlexBalloonTip
import net.java.balloontip.FlexBalloonTip.{ Orientation, AttachLocation }
import net.java.balloontip.styles.RoundedBalloonStyle
import javax.swing.{JComponent, JComboBox, DefaultComboBoxModel}
import net.miginfocom.swing.MigLayout
import scalaj.collection.Imports._

class PlaneBalloonView(attach : JComponent) extends BalloonView(attach) {
  val l = List(LaunchItem("self_launch"), LaunchItem("wire_launch"), LaunchItem("tow_launch"))
  val registration = new TextField
  val make = new TextField
  val group = new MyComboBox[Group](new MyComboBoxModel(AllGroups))
  val defaultLaunchMethod = new MyComboBox(new DefaultComboBoxModel(new java.util.Vector(l.asJava)))
  val legalPlaneClass = new MyComboBox[LegalPlaneClass](new MyComboBoxModel(AllLegalPlaneClasses))
  val hasEngine = new CheckBox(I18n("has_engine"))
  val canFlyWithoutEngine = new CheckBox(I18n("can_fly_without_engine"))
  val canTow = new CheckBox(I18n("can_tow"))
  val selflaunching = new CheckBox(I18n("selflaunching"))
  val canBeTowed = new CheckBox(I18n("can_be_towed"))
  val canBeWireLaunched = new CheckBox(I18n("can_be_wire_launched"))
  val defaultEngineDurationToDuration = new CheckBox(I18n("default_engine_duration_to_duration"))
  val btOk = new Button(I18n("ok"))
  val btCancel = new Button(I18n("cancel"))
  setLayout(new MigLayout("fill, wrap 2"))
  add(new Label(I18n("plane_balloon.title")).peer, "span 2")
  add(new Label(I18n("registration")).peer)
  add(registration.peer, "w 200")
  add(new Label(I18n("make")).peer)
  add(make.peer, "w 200")
  add(new Label(I18n("group")).peer)
  add(group.peer, "w 150")
  add(new Label(I18n("legal_plane_class")).peer)
  add(legalPlaneClass.peer, "w 150")
  add(new Label(I18n("default_launch_method")).peer)
  add(defaultLaunchMethod.peer, "w 150")
  add(hasEngine.peer, "skip 1")
  add(canFlyWithoutEngine.peer, "skip 1")
  add(selflaunching.peer, "skip 1")
  add(canTow.peer, "skip 1")
  add(canBeTowed.peer, "skip 1")
  add(canBeWireLaunched.peer, "skip 1")
  add(defaultEngineDurationToDuration.peer, "skip 1")
  add(btOk.peer)
  add(btCancel.peer)
  init
  registration.peer.requestFocusInWindow
}

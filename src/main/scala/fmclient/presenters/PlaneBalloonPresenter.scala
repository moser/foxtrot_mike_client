package fmclient.presenters

import fmclient.views.PlaneBalloonView
import fmclient.models.{ Plane, LaunchItem }
import javax.swing.JComponent
import scala.swing.event._
import scala.swing.Publisher


object PlaneBalloonPresenter {
  case class OkEvent(obj : Plane) extends Event
  case class CancelEvent() extends Event
}

class PlaneBalloonPresenter(str : String, val attach : JComponent, view0: PlaneBalloonView) extends BasePresenter[Plane, PlaneBalloonView] with Publisher {
  def this(str : String, attach : JComponent) = this(str, attach, new PlaneBalloonView(attach))
  var view = view0
  var model : Plane = new Plane

  view.registration.text = str.toUpperCase

  map((m,v) => m.registration = v.registration.text, (m,v) => v.registration.text = m.registration)
  map((m,v) => m.make = v.make.text, (m,v) => v.make.text = m.make)
  map((m,v) => m.group = v.group.selection.item, (m,v) => v.group.selection.item = m.group)
  map((m,v) => m.legalPlaneClass = v.legalPlaneClass.selection.item, (m,v) => v.legalPlaneClass.selection.item = m.legalPlaneClass)
  map((m,v) => {
      v.defaultLaunchMethod.selection.item match {
        case LaunchItem("self_launch") => m.defaultLaunchMethod = "self"
        case LaunchItem(str) => m.defaultLaunchMethod = str
      }
    },
    (m,v) => {
      m.defaultLaunchMethod match {
        case "self" => v.defaultLaunchMethod.selection.item = LaunchItem("self_launch")
        case str => v.defaultLaunchMethod.selection.item = LaunchItem(str)
      }
    })
  map((m,v) => m.hasEngine = v.hasEngine.selected, (m,v) => v.hasEngine.selected = m.hasEngine)
  map((m,v) => m.canFlyWithoutEngine = v.canFlyWithoutEngine.selected, (m,v) => v.canFlyWithoutEngine.selected = m.canFlyWithoutEngine)
  map((m,v) => m.canTow = v.canTow.selected, (m,v) => v.canTow.selected = m.canTow)
  map((m,v) => m.canBeTowed = v.canBeTowed.selected, (m,v) => v.canBeTowed.selected = m.canBeTowed)
  map((m,v) => m.canBeWireLaunched = v.canBeWireLaunched.selected, (m,v) => v.canBeWireLaunched.selected = m.canBeWireLaunched)
  map((m,v) => m.defaultEngineDurationToDuration = v.defaultEngineDurationToDuration.selected, (m,v) => v.defaultEngineDurationToDuration.selected = m.defaultEngineDurationToDuration)

  view.btOk.reactions += {
    case ButtonClicked(_) => {
      updateModel
      model.save
      destroy
      publish(PlaneBalloonPresenter.OkEvent(model))
    }
  }

  view.btCancel.reactions += {
    case ButtonClicked(_) => {
      destroy
      publish(PlaneBalloonPresenter.CancelEvent())
    }
  }

  def destroy = {
    view.closeBalloon
    attach.setEnabled(true)
    attach.requestFocusInWindow
  }
}

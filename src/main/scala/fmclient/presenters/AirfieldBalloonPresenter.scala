package fmclient.presenters

import fmclient.views.AirfieldBalloonView
import fmclient.models.Airfield
import javax.swing.JComponent
import scala.swing.event._
import scala.swing.Publisher


object AirfieldBalloonPresenter {
  case class OkEvent(obj : Airfield) extends Event
  case class CancelEvent() extends Event
}

class AirfieldBalloonPresenter(str : String, val attach : JComponent, view0: AirfieldBalloonView) extends BasePresenter[Airfield, AirfieldBalloonView] with Publisher {
  def this(str : String, attach : JComponent) = this(str, attach, new AirfieldBalloonView(attach))
  var view = view0
  var model : Airfield = new Airfield

  view.name.text = str

  map((m,v) => m.name = v.name.text, (m,v) => v.name.text = m.name)
  map((m,v) => m.registration = v.registration.text, (m,v) => v.registration.text = m.registration)

  view.btOk.reactions += {
    case ButtonClicked(_) => {
      updateModel
      model.save
      destroy
      publish(AirfieldBalloonPresenter.OkEvent(model))
    }
  }

  view.btCancel.reactions += {
    case ButtonClicked(_) => {
      destroy
      publish(AirfieldBalloonPresenter.CancelEvent())
    }
  }

  def destroy = {
    view.closeBalloon
    attach.setEnabled(true)
    attach.requestFocusInWindow
  }
}

package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.PersonBalloonView
import de.moserei.foxtrotmike.client.models.Person
import javax.swing.JComponent
import scala.swing.event._
import scala.swing.Publisher


object PersonBalloonPresenter {
  case class OkEvent(obj : Person) extends Event
  case class CancelEvent() extends Event
}

class PersonBalloonPresenter(str : String, val attach : JComponent, view0: PersonBalloonView) extends BasePresenter[Person, PersonBalloonView] with Publisher {
  def this(str : String, attach : JComponent) = this(str, attach, new PersonBalloonView(attach))  
  var view = view0
  var model : Person = new Person
  
  var spl = str.split(" ")
  view.firstname.text = spl(0)
  if(spl.length > 1) {
    view.lastname.text = spl(1)
  }
  
  map((m,v) => m.firstname = v.firstname.text, (m,v) => v.firstname.text = m.firstname)
  map((m,v) => m.lastname = v.lastname.text, (m,v) => v.lastname.text = m.lastname)
  
  view.btOk.reactions += {
    case ButtonClicked(_) => {
      updateModel
      model.save
      destroy
      publish(PersonBalloonPresenter.OkEvent(model))
    }
  }
  
  view.btCancel.reactions += {
    case ButtonClicked(_) => {
      destroy
      publish(PersonBalloonPresenter.CancelEvent())
    }
  }
  
  def destroy = {
    view.closeBalloon
    attach.setEnabled(true)
    attach.requestFocusInWindow
  }
}

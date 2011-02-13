package de.moserei.foxtrotmike.client.presenters

import scala.swing.{TextComponent, AbstractButton, Reactor}
import scala.swing.event.ButtonClicked
import org.joda.time.DateTime

class TimeSetter(presenter : BasePresenter[_,_]) extends Reactor {
  var mappings = Map[AbstractButton, TextComponent]()
  
  def add(b:AbstractButton, t:TextComponent) = {
    listenTo(b)
    mappings = mappings + new Tuple2(b,t)
  }

  def setCurrentTime(where:TextComponent) = {
    var dt = new DateTime
    if(dt.getSecondOfMinute > 30) { dt = dt.plusMinutes(1) }
    where.text = String.format("%d:%02d", dt.getHourOfDay.asInstanceOf[AnyRef], dt.getMinuteOfHour.asInstanceOf[AnyRef])
    presenter.updateModel
    presenter.updateView
  }
  
  reactions += {
    case ButtonClicked(c) => {
      setCurrentTime(mappings(c))
    }
  }
  
}

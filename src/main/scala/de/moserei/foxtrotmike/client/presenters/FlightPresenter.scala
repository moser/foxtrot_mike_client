package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.{ FlightView, MigPanel, MyFormattedTextField }
import de.moserei.foxtrotmike.client.models.{ Flight, WireLaunch, TowLaunch, LaunchItem }
import de.moserei.foxtrotmike.client.views.AutoCompleter.CreateEvent
import org.joda.time.DateTime
import scala.swing.event._
import scala.swing.{Component, TextComponent}
import java.util.Date
import swing.Reactor
import java.awt.{ Color, Component => AWTComponent }


class FlightPresenter(view0: FlightView) extends BasePresenter[Flight, FlightView] {
  def this() = this(new FlightView())  
  var view = view0
  protected var _model : Flight = _
  def model = _model
  def model_=(f:Flight) = {
    if(f == null) { view.enabled = false }
    if(f != null) { view.enabled = true }
    _model = f
    if(f != null) {
      updateView
    }
  }
  model = null
  var wireLaunchPresenter : WireLaunchPresenter = _
  var towLaunchPresenter : TowLaunchPresenter = _

  map((m,v) => m.plane = v.plane.selectedItem, (m,v) => v.plane.selectedItem = m.plane)
  map((m,v) => m.seat1 = v.seat1.selectedItem, (m,v) => v.seat1.selectedItem = m.seat1)
  map((m,v) => m.seat2 = v.seat2.selectedItem, (m,v) => v.seat2.selectedItem = m.seat2)
  map((m,v) => m.controller = v.controller.selectedItem, (m,v) => v.controller.selectedItem = m.controller)
  map((m,v) => m.from = v.from.selectedItem, (m,v) => v.from.selectedItem = m.from)
  map((m,v) => m.to = v.to.selectedItem, (m,v) => v.to.selectedItem = m.to)
  map((m,v) => m.departureDate = v.departureDate.peer.getValue.asInstanceOf[Date], (m,v) => v.departureDate.peer.setValue(m.departureDate))
  map((m,v) => m.arrivalTime = v.arrivalTime.peer.getValue.asInstanceOf[Int], (m,v) => v.arrivalTime.peer.setValue(m.arrivalTime))
  map((m,v) => m.departureTime = v.departureTime.peer.getValue.asInstanceOf[Int], (m,v) => v.departureTime.peer.setValue(m.departureTime))
  mapViewOnly((m,v) => v.duration.text = m.durationString)
  mapViewOnly((m,v) => markIfInvalid(v.plane, m.isPlaneValid)) 
  mapViewOnly((m,v) => markIfInvalid(v.seat1, m.isSeat1Valid))
  mapViewOnly((m,v) => markIfInvalid(v.from, m.isFromValid))
  mapViewOnly((m,v) => markIfInvalid(v.to, m.isToValid))
  mapViewOnly((m,v) => markIfInvalid(v.departureTime, m.isDepartureTimeValid))
  mapViewOnly((m,v) => markIfInvalid(v.controller, m.isControllerValid))
  
  
  private def markIfInvalid(c : Component, valid : Boolean) : Unit = {
    markIfInvalid(c.peer, valid)
  }
  
  private def markIfInvalid(c : AWTComponent, valid : Boolean) : Unit = {
    if(!valid) {
      c.setBackground(new Color(231, 122, 122)) //TODO create an object that holds all the used colors
    } else {
      c.setBackground(null)
    }
  }
  
  view.launchType.selection.reactions += {
    case SelectionChanged(_) => {
      updateModel
      model.launchType = view.launchType.selection.item.back
      updateView
    }
  }

  view.btSave.reactions += {
    case ButtonClicked(_) => {
      updateModel
      model.save
      updateView
    }
  }
  
  view.seat1.reactions += {
    case CreateEvent(str, old) => {
      println("create")
      view.seat1.setEnabled(false)
      val bp = new PersonBalloonPresenter(str, view.seat1)
      bp.reactions += {
        case PersonBalloonPresenter.OkEvent(o) => {
          view.seat1.selectedItem = o
        }
        case PersonBalloonPresenter.CancelEvent() => {
          view.seat1.revertLast
        }
      }
    }
  }
  
  val timeSetter = new TimeSetter(this) { 
    add(view.btDepartureTime, view.departureTime)
    add(view.btArrivalTime, view.arrivalTime)
  }

  val timeFocus = new Reactor {
    listenTo(view.departureTime, view.arrivalTime)
    reactions += {
      case FocusLost(_, _, _) => {
        updateModel
        updateView
      }
    }
  }
  
  val validationFocus = new Reactor {
    listenTo(view.plane, view.seat1, view.from, view.to, view.controller)
    reactions += {
      case FocusLost(_, _, _) => {
        println("validationFocus")
        updateModel
        updateView
      }
    }
  }
  
  view.reactions += {
    case FocusLost(_, _, _) => {
      println("flight lost focus")
    }
  }
  
  override def updateModel() = {
    super.updateModel
    if(wireLaunchPresenter != null) { wireLaunchPresenter.updateModel }
    if(towLaunchPresenter != null) { towLaunchPresenter.updateModel }
  }
  
  override def updateView = {
    super.updateView
    view.launchType.selection.item = LaunchItem(model.launchType)
    view.launchType.selection.item match {
      case LaunchItem("WireLaunch") => {
        towLaunchPresenter = null
        wireLaunchPresenter = new WireLaunchPresenter
        wireLaunchPresenter.model = model.launch.asInstanceOf[WireLaunch]
        view.launchPanel = wireLaunchPresenter.view
        wireLaunchPresenter.updateView
      } 
      case LaunchItem("TowLaunch") => {
        wireLaunchPresenter = null
        towLaunchPresenter = new TowLaunchPresenter
        towLaunchPresenter.model = model.launch.asInstanceOf[TowLaunch]
        view.launchPanel = towLaunchPresenter.view
        towLaunchPresenter.updateView
      }
      case LaunchItem(_) => { 
        wireLaunchPresenter = null
        towLaunchPresenter = null
        view.launchPanel = new MigPanel("ins 0")
      }
    }
  }
  
  override def shutdown = {
    println("shutdown")
    if(model != null) {
      updateModel
      model.save
    }
  }
}

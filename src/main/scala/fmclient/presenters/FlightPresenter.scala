package fmclient.presenters

import fmclient.views.{ FlightView, MigPanel, MyFormattedTextField, AutoCompleter, Colors }
import fmclient.models.{ Flight, WireLaunch, TowLaunch, LaunchItem, Seat1ACModel, Person, Seat2ACModel, Airfield, Liability, ProportionItem, Plane }
import fmclient.models.repos.AllPeople
import fmclient.views.AutoCompleter._
import org.joda.time.DateTime
import scala.swing.event._
import java.util.Date
import swing.Reactor
import scala.collection.mutable.ArraySeq
import javax.swing.SwingUtilities


class FlightPresenter(view0: FlightView) extends BasePresenter[Flight, FlightView] {
  def this() = this(new FlightView())
  var view = view0
  protected var _model : Flight = _
  def model = _model
  def model_=(f:Flight) = {
    if(model != null && !model.deleted) {
      updateModel
      model.save
    }
    if(f == null) { view.enabled = false }
    if(f != null) { view.enabled = true }
    _model = f
    if(f != null) {
      updateView
     // view.departureDate.requestFocusInWindow
    }
  }
  model = null
  var wireLaunchPresenter : WireLaunchPresenter = _
  var towLaunchPresenter : TowLaunchPresenter = _

  map((m,v) => m.plane = v.plane.selectedOption.get, (m,v) => v.plane.selectedOption = m.plane)

  map((m,v) => { //to model
        if(v.seat1.selectedOption.isInstanceOf[RealOption[Person]]) {
          m.seat1 = v.seat1.selectedOption.get
          m.seat1Unknown = false
        } else if(v.seat1.selectedOption.isInstanceOf[Seat1ACModel.UnknownPersonOption]) {
          m.seat1 = null
          m.seat1Unknown = true
        }
      },
      (m,v) => { //to view
        if(!m.seat1Unknown) {
          v.seat1.selectedOption = m.seat1
        } else {
          v.seat1.selectedOption = new Seat1ACModel.UnknownPersonOption
        }
      })

  map((m,v) => { //to model
        if(v.seat2.selectedOption.isInstanceOf[RealOption[Person]] || v.seat2.selectedOption.isInstanceOf[NilOption[Person]]) {
          m.seat2 = v.seat2.selectedOption.get
          m.seat2Number = 0
        } else if(v.seat2.selectedOption.isInstanceOf[Seat2ACModel.NumberOption]) {
          m.seat2 = null
          m.seat2Number = v.seat2.selectedOption.asInstanceOf[Seat2ACModel.NumberOption].n
        }
      },
      (m,v) => { //to view
        if(m.seat2Number <= 0) {
          v.seat2.selectedOption = m.seat2
        } else {
          v.seat2.selectedOption = new Seat2ACModel.NumberOption(m.seat2Number)
        }
      })
  map((m,v) => m.controller = v.controller.selectedOption.get, (m,v) => v.controller.selectedOption = m.controller)
  map((m,v) => m.from = v.from.selectedOption.get, (m,v) => v.from.selectedOption = m.from)
  map((m,v) => m.to = v.to.selectedOption.get, (m,v) => v.to.selectedOption = m.to)
  map((m,v) => m.costHint = v.costHint.selection.item, (m,v) => v.costHint.selection.item = m.costHint)
  map((m,v) => m.comment = v.comment.text, (m,v) => v.comment.text = m.comment)
  map((m,v) => m.departureDate = v.departureDate.peer.getValue.asInstanceOf[Date], (m,v) => v.departureDate.peer.setValue(m.departureDate))
  map((m,v) => m.arrivalTime = v.arrivalTime.peer.getValue.asInstanceOf[Int], (m,v) => v.arrivalTime.peer.setValue(m.arrivalTime))
  map((m,v) => m.departureTime = v.departureTime.peer.getValue.asInstanceOf[Int], (m,v) => v.departureTime.peer.setValue(m.departureTime))
  map((m,v) => m.engineDuration = v.engineDuration.peer.getValue.asInstanceOf[Int], (m,v) => v.engineDuration.peer.setValue(m.engineDuration))
  mapViewOnly((m,v) => v.duration.text = m.durationString)

  mapViewOnly((m,v) => markIfInvalid(v.plane, m.isPlaneValid))
  mapViewOnly((m,v) => markIfInvalid(v.seat1, m.isSeat1Valid))
  mapViewOnly((m,v) => markIfInvalid(v.from, m.isFromValid))
  mapViewOnly((m,v) => markIfInvalid(v.to, m.isToValid))
  mapViewOnly((m,v) => markIfInvalid(v.departureTime, m.isDepartureTimeValid))

  mapViewOnly((m,v) => markIfHasProblems(v.launchType, m.launchTypeHasProblems))
  mapViewOnly((m,v) => markIfHasProblems(v.departureDate, m.departureDateHasProblems))

  mapViewOnly((m,v) => v.engineDuration.enabled = m.engineDurationPossible)
  map((m,v) => { //to model
        updateLiabilitesModel
      },
      (m,v) => { //to view
        updateLiabilitesView
      })


  view.launchType.selection.reactions += {
    case SelectionChanged(_) => {
      if(!updatingView) {
        updateModel
        model.launchType = view.launchType.selection.item.back
        updateView
      }
    }
  }

  view.btSave.reactions += {
    case ButtonClicked(_) => {
      updateModel
      model.save
      updateView
    }
  }

  view.plane.reactions += {
    case CreateEvent(c, str, old) => {
      view.plane.setEnabled(false)
      val bp = new PlaneBalloonPresenter(str, view.plane)
      bp.reactions += {
        case PlaneBalloonPresenter.OkEvent(o) => {
          view.plane.selectedOption = o
        }
        case PlaneBalloonPresenter.CancelEvent() => {
          view.plane.revertLast
        }
      }
    }
    case SelectionChangedEvent(_, n) => {
      if(!updatingView && model.plane == null && n.isInstanceOf[RealOption[Plane]]) {
        SwingUtilities.invokeLater(new Runnable {
          def run {
            val p = n.asInstanceOf[RealOption[Plane]].get
            //view.launchType.selection.item = LaunchItem(p.defaultLaunchMethod)
            //updateModel
            model.plane = p
            model.launchType = p.defaultLaunchMethod
            updateView
          }
        })
      }
    }
  }

  val seatCreate = new Reactor {
    listenTo(view.seat1, view.seat2, view.controller)
    reactions += {
      case CreateEvent(c, str, old) => {
        c.setEnabled(false)
        val bp = new PersonBalloonPresenter(str, c)
        bp.reactions += {
          case PersonBalloonPresenter.OkEvent(o) => {
            c.selectedOption = o
          }
          case PersonBalloonPresenter.CancelEvent() => {
            c.revertLast
          }
        }
      }
    }
  }

  val timeSetter = new TimeSetter(this) {
    add(view.btDepartureTime, view.departureTime)
    add(view.btArrivalTime, view.arrivalTime)
  }

  val validationFocus = new Reactor {
    listenTo(view.plane, view.seat1)
    reactions += {
      case FocusLostEvent() => {
        updateModel
        updateView
      }
    }
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

  private var liabilitiesUpdating = false
  val liabilitySelection = new Reactor {
    for(i <- 0 to 3)
      listenTo(view.liability(i))
    reactions += {
      case ActionEvent(c) => {
        if(!liabilitiesUpdating) {
          val i = view.liability.indexOf(c)
          if(view.liability(i).selected) {
            view.liablePerson(i).enabled = true
            view.proportion(i).enabled = true
            view.liablePerson(i).selection.item = view.liablePerson(i).model.getElementAt(0).asInstanceOf[Person]
          } else {
            updateLiabilitesModel
            updateLiabilitesView
          }
        }
      }
    }
  }

  val liablePersonFocus = new Reactor {
    for(i <- 0 to 3) {
      listenTo(view.liablePerson(i).selection)
      listenTo(view.proportion(i).selection)
    }
    reactions += {
      case SelectionChanged(_) => {
        if(!liabilitiesUpdating && model != null) {
          updateLiabilitesModel
          updateLiabilitesView
        }
      }
    }
  }
  
  val airfieldCreate = new Reactor {
    listenTo(view.from, view.to)
    reactions += {
      case CreateEvent(c, str, _) => {
        val bp = new AirfieldBalloonPresenter(str, c)
        bp.reactions += {
          case AirfieldBalloonPresenter.OkEvent(o) => {
            c.selectedOption = o
          }
          case AirfieldBalloonPresenter.CancelEvent() => {
            c.revertLast
          }
        }
      }
    }
  }

  def updateLiabilitesModel() {
    var a = ArraySeq[Liability]()
    for(i <- 0 to 3) {
      if(view.liability(i).selected) {
        a = a ++ ArraySeq(new Liability(view.liablePerson(i).selection.item.id, view.proportion(i).selection.item.p))
      }
    }
    model.liabilities = a
  }

  def updateLiabilitesView() {
    if(!liabilitiesUpdating) {
      liabilitiesUpdating = true
      var firstUnchecked = true
      for(i <- 0 to 3) {
        if(i < model.liabilities.length) {
          view.liability(i).selected = true
          view.liability(i).enabled = true
          view.liablePerson(i).selection.item = AllPeople.find(model.liabilities(i).personId)
          view.liablePerson(i).enabled = true
          view.proportion(i).selection.item = new ProportionItem(model.liabilities(i).proportion)
          view.proportion(i).enabled = true
          view.realProportion(i).text = (model.proportionFor(model.liabilities(i)) * 100.0).toInt.toString + "%"
        } else {
          view.liability(i).selected = false
          view.liability(i).enabled = false
          val x : Person = null
          view.liablePerson(i).selection.item = x
          view.liablePerson(i).enabled = false
          view.proportion(i).selection.item = new ProportionItem(1)
          view.proportion(i).enabled = false
          view.realProportion(i).text = ""
          if(firstUnchecked) {
            firstUnchecked = false
            view.liability(i).enabled = true
          }
        }
      }
      liabilitiesUpdating = false
    }
  }

  override def updateModel() = {
    super.updateModel
    if(wireLaunchPresenter != null) { wireLaunchPresenter.updateModel }
    if(towLaunchPresenter != null) { towLaunchPresenter.updateModel }
  }

  private var updatingView = false
  override def updateView = {
    if(!updatingView) {
      updatingView = true
      super.updateView
      view.launchType.selection.item = LaunchItem(model.launchType)
      view.launchType.selection.item match {
        case LaunchItem("wire_launch") => {
          towLaunchPresenter = null
          wireLaunchPresenter = new WireLaunchPresenter
          wireLaunchPresenter.model = model.launch.asInstanceOf[WireLaunch]
          view.launchPanel = wireLaunchPresenter.view
          wireLaunchPresenter.updateView
        }
        case LaunchItem("tow_launch") => {
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
      updatingView = false
    }
  }

  override def shutdown = {
    if(model != null) {
      updateModel
      model.save
    }
  }
}

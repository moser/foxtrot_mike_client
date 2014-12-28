package fmclient.presenters

import fmclient.views.MainView
import scala.swing.event._
import fmclient.models.{DefaultsSingleton, Flight, EntityMgr, Summary, SummaryReceived, I18n}

class MainPresenter extends AbstractPresenter {
  private var updating = false
  val flightPresenter = new FlightPresenter()

  val view = new MainView(flightPresenter.view)
  val summary = new Summary()
  summary.reactions += {
    case SummaryReceived(counts) => {
      val planeStrings = counts.toSeq.sortBy(_._2).map {
        case (registration, count) => s"${registration} (${count})"
      }
      view.setStatus(s"${I18n("main.status.summary")}: ${planeStrings.mkString(", ")}")
    }
  }
  summary.get

  val defaultsPresenter = new DefaultsPresenter(view.defaultsPanel)
  val syncPresenter = new SyncPresenter(view.syncPanel, this)

  view.reactions += {
    case WindowClosed(view) => {
      shutdown
    }
  }

  view.visible = true
  view.centerOnScreen

  view.flightsTable.selection.reactions += {
    case TableRowsSelected(_, r, false) => {
      if(!view.flightsTable.selection.rows.isEmpty && !updating) {
        selectOrNull(view.flightsTable.selection.rows.head)
      }
    }
  }

  view.flightsTable.reactions += {
    case TableChanged(_) => {
      var i = view.flightsTableModel.indexOf(flightPresenter.model)
      if(i >= 0 && view.flightsTableModel.getRowCount > i) {
        view.flightsTable.selection.rows.add(i)
      }
    }
  }

  view.btNew.reactions += {
    case ButtonClicked(_) => {
      val f = new Flight(DefaultsSingleton)
      f.save
      view.flightsTable.selection.rows.clear
      view.flightsTable.selection.rows.add(view.flightsTableModel.indexOf(f))
      flightPresenter.requestFocus
    }
  }

  view.btCopy.reactions += {
    case ButtonClicked(_) => {
      if(flightPresenter.model != null) {
        val f = new Flight(flightPresenter.model)
        f.save
        view.flightsTable.selection.rows.clear
        view.flightsTable.selection.rows.add(view.flightsTableModel.indexOf(f))
        flightPresenter.requestFocus
      }
    }
  }

  flightPresenter.view.btDelete.reactions += {
    case ButtonClicked(_) => {
      flightPresenter.model.delete
      view.flightsTable.selection.rows.clear
      selectFirstOrNull
    }
  }

  view.unfinishedOnly.reactions += {
    case ButtonClicked(_) => {
      view.flightsTableModel.unfinishedOnly = view.unfinishedOnly.selected
      selectFirstOrNull
    }
  }

  view.problemsOnly.reactions += {
    case ButtonClicked(_) => {
      view.flightsTableModel.problemsOnly = view.problemsOnly.selected
      selectFirstOrNull
    }
  }

  view.colored.reactions += {
    case ButtonClicked(_) => {
      view.flightsTable.repaint
    }
  }

  def invalidateFTM {
    view.flightsTableModel.invalidate
  }

  private def selectOrNull(i : Int) {
    updating = true
    if(i >= 0 && view.flightsTableModel.getRowCount > i)  {
      flightPresenter.model = view.flightsTableModel.getAll.apply(i)
      view.currentIdx = i
      view.flightsTable.selection.rows.add(i)
      view.btCopy.enabled = true
    } else {
      flightPresenter.model = null
      view.currentIdx = -1
      view.flightsTable.selection.rows.dropWhile((i) => true)
      view.btCopy.enabled = false
    }
    updating = false
  }

  def selectFirstOrNull {
    selectOrNull(0)
  }

  private def shutdown = {
    flightPresenter.shutdown
    EntityMgr.close
  }
}

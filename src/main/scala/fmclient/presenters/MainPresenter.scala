package fmclient.presenters

import fmclient.views.MainView
import scala.swing.event._
import fmclient.models.{DefaultsSingleton, Flight, EntityMgr}

class MainPresenter extends AbstractPresenter {
  val view = new MainView()

  view.reactions += {
    case WindowClosed(view) => {
      shutdown
    }
  }

  view.visible = true
  var fp = new FlightPresenter(view.flightPanel)
  var dp = new DefaultsPresenter(view.defaultsPanel)
  val sp = new SyncPresenter

  private var updating = false
  view.flightsTable.selection.reactions += {
    case TableRowsSelected(_, r, false) => {
      if(!view.flightsTable.selection.rows.isEmpty && !updating) {
        selectOrNull(view.flightsTable.selection.rows.head)
      }
    }
  }

  view.flightsTable.reactions += {
    case TableChanged(_) => {
      var i = view.flightsTableModel.indexOf(fp.model)
      if(i > 0 && view.flightsTableModel.getRowCount > i) {
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
      view.flightPanel.departureDate.requestFocusInWindow
    }
  }

  view.btCopy.reactions += {
    case ButtonClicked(_) => {
      if(fp.model != null) {
        val f = new Flight(fp.model)
        f.save
        view.flightsTable.selection.rows.clear
        view.flightsTable.selection.rows.add(view.flightsTableModel.indexOf(f))
        view.flightPanel.departureDate.requestFocusInWindow
      }
    }
  }

  fp.view.btDelete.reactions += {
    case ButtonClicked(_) => {
      fp.model.delete
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

  view.btSync.reactions += {
    case ButtonClicked(_) => {
      sp.view.open
    }
  }

  private def selectOrNull(i : Int) {
    updating = true
    if(i >= 0 && view.flightsTableModel.getRowCount > i)  {
      fp.model = view.flightsTableModel.getAll.apply(i)
      view.flightsTable.selection.rows.add(i)
      view.btCopy.enabled = true
    } else {
      fp.model = null
      view.flightsTable.selection.rows.dropWhile((i) => true)
      view.btCopy.enabled = false
    }
    updating = false
  }

  private def selectFirstOrNull {
    selectOrNull(0)
  }

  private def shutdown = {
    fp.shutdown
    EntityMgr.close
  }

  sp.view.open
}

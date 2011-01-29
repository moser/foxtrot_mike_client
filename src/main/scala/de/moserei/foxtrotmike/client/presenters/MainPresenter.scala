package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.MainView
import scala.swing.event._
import de.moserei.foxtrotmike.client.models.{DefaultsSingleton, Flight}

class MainPresenter {
  val view = new MainView()
  view.visible = true
  var fp = new FlightPresenter(view.flightPanel)
  var dp = new DefaultsPresenter(view.defaultsPanel)
  val sp = new SyncPresenter
  
  view.flightsTable.selection.reactions += {
    case TableRowsSelected(_, r, false) => {
      if(!view.flightsTable.selection.rows.isEmpty) {
        var i = view.flightsTable.selection.rows.head
        if(i >= 0) {
          fp.model = view.flightsTableModel.getAll.apply(i)
          fp.updateView
        }
      }
    }
  }

  view.flightsTable.reactions += {
    //case TableChanged(_) => println("tc")
    case TableUpdated(_, _, _) => {
      if(fp.model != null) {
        view.flightsTable.selection.rows.clear
        view.flightsTable.selection.rows.add(view.flightsTableModel.indexOf(fp.model))
      }
    }
    //case TableStructureChanged(_) => println("tsc")
  }
  
  view.flightPanel.btNew.reactions += {
    case ButtonClicked(_) => {
      val f = new Flight(DefaultsSingleton)
      f.save
      view.flightsTable.selection.rows.clear
      view.flightsTable.selection.rows.add(view.flightsTableModel.indexOf(f))
      view.flightPanel.departureDate.requestFocusInWindow
    }
  }
  
  view.unfinishedOnly.reactions += {
    case ButtonClicked(_) => {
      view.flightsTableModel.unfinishedOnly = view.unfinishedOnly.selected
    }
  }

  def nop {}  
  
  sp.view.open
}

package de.moserei.foxtrotmike.client.views

import swing._
import de.moserei.foxtrotmike.client.models.I18n
import de.moserei.foxtrotmike.client.models.FlightsTableModel

class MainView extends MainFrame {
  title = I18n("main.title")
  preferredSize = new Dimension(1000, 650)
  val tabs = new TabbedPane
  val flightsTableModel = new FlightsTableModel
  val flightsTable = new Table {
    model = flightsTableModel
    selection.intervalMode = Table.IntervalMode.Single
    selection.elementMode = Table.ElementMode.Row
  }
  val flightsPanel = new MigPanel("ins 0, fill", "", "[25!][]") {
    add(new CheckBox(I18n("flying-only")))
    add(new CheckBox(I18n("with-problems-only")))
    add(new CheckBox(I18n("colored")), "wrap")
    add(new ScrollPane(flightsTable), "gap 0, dock south, grow")
  }
  
  val flightPanel = new FlightView
  val defaultsPanel = new DefaultsView
  
  tabs.pages += new TabbedPane.Page(I18n("flight"), flightPanel)
  tabs.pages += new TabbedPane.Page(I18n("defaults"), defaultsPanel)
  
  contents = new SplitPane {
    dividerSize = 12
    orientation = Orientation.Horizontal
    rightComponent = flightsPanel
    leftComponent = tabs
  }
}

package de.moserei.foxtrotmike.client.views

import swing._
import de.moserei.foxtrotmike.client.models.I18n
import de.moserei.foxtrotmike.client.models.FlightsTableModel

class MainView extends MainFrame {
  title = I18n.t("main.title")
  preferredSize = new Dimension(1000, 650)
  val tabs = new TabbedPane
  val flightsTableModel = new FlightsTableModel
  val flightsTable = new Table {
    model = flightsTableModel
    selection.intervalMode = Table.IntervalMode.Single
    selection.elementMode = Table.ElementMode.Row
  }
  val flightPanel = new FlightView
  val defaultsPanel = new DefaultsView
  tabs.pages += new TabbedPane.Page(I18n("defaults.tab"), defaultsPanel)
  contents = new SplitPane {
    dividerSize = 12
    oneTouchExpandable = true
    resizeWeight = 0.9
    orientation = Orientation.Vertical
    leftComponent = new SplitPane {
      dividerSize = 12
      orientation = Orientation.Horizontal
      rightComponent = new ScrollPane(flightsTable)
      leftComponent = flightPanel
    }
    rightComponent = tabs
  }
}

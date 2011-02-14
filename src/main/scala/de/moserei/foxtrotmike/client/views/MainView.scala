package de.moserei.foxtrotmike.client.views

import swing._
import swing.event._
import de.moserei.foxtrotmike.client.models.I18n
import de.moserei.foxtrotmike.client.models.FlightsTableModel
import java.awt.Color

class MainView extends MainFrame {
  private val colInvalid = new Color(231, 122, 122)
  private val colInvalidSelected = new Color(231, 40, 40)
  
  private val colUnfinished = new Color(122, 122, 231)
  private val colUnfinishedSelected = new Color(40, 40, 231)
  
  private val col = new Color(122, 231, 122)
  private val colSelected = new Color(40, 231, 40)

  title = I18n("main.title")
  preferredSize = new Dimension(1000, 650)
  val tabs = new TabbedPane
  val flightsTableModel = new FlightsTableModel
  val flightsTable = new Table {
    model = flightsTableModel
    selection.intervalMode = Table.IntervalMode.Single
    selection.elementMode = Table.ElementMode.Row

    override def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int): Component = {
      val c = super.rendererComponent(isSelected, focused, row, column)
      if(!colored.selected) {
        if(isSelected) {
          c.background = selectionBackground
	        c.foreground = selectionForeground
    	  } else {
    	    c.background = background
          c.foreground = foreground
    	  }
    	} else {
    	  if(isSelected) {
          if(!flightsTableModel.get(row).finished) {
            c.background = colUnfinishedSelected
          } else if(!flightsTableModel.get(row).isValid) {
            c.background = colInvalidSelected
          } else {
            c.background = colSelected
          }
          c.foreground = selectionForeground
    	  } else {
	        if(!flightsTableModel.get(row).finished) {
            c.background = colUnfinished
          } else if(!flightsTableModel.get(row).isValid) {
            c.background = colInvalid
          } else {
            c.background = col
          }
          c.foreground = foreground
    	  }
	    }
      c
    }
  }
  val unfinishedOnly = new CheckBox(I18n("flying-only"))
  val problemsOnly = new CheckBox(I18n("with-problems-only"))
  val colored = new CheckBox(I18n("colored"))
  val btNew = new Button(I18n("new"))
  val btCopy = new Button(I18n("copy"))
  val flightsPanel = new MigPanel("ins 0, fill", "", "[grow 0][fill]") {
    add(btNew)
    add(btCopy)
    add(unfinishedOnly)
    add(problemsOnly)
    add(colored, "wrap")
    add(new ScrollPane(flightsTable), "gap 0, span, w 100%")
  }
  
  val flightPanel = new FlightView
  val defaultsPanel = new DefaultsView
  
  val btSync = new Button(I18n("sync"))
  val actionPanel = new MigPanel {
    add(btSync)
  }
  
  tabs.pages += new TabbedPane.Page(I18n("flight"), flightPanel)
  tabs.pages += new TabbedPane.Page(I18n("defaults"), defaultsPanel)
  tabs.pages += new TabbedPane.Page(I18n("actions"), actionPanel)
  
  contents = new SplitPane {
    dividerSize = 12
    orientation = Orientation.Horizontal
    rightComponent = flightsPanel
    leftComponent = tabs
  }
}

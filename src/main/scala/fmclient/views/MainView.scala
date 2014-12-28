package fmclient.views

import javax.swing.{KeyStroke, AbstractAction, JComponent}
import java.awt.event.ActionEvent

import swing._
import swing.event._

import fmclient.models.I18n
import fmclient.models.FlightsTableModel

class MainView(val flightPanel : FlightView) extends MainFrame {
  title = I18n("main.title")
  preferredSize = new Dimension(1000, 650)
  minimumSize = new Dimension(900, 450)

  var currentIdx = -1

  val tabs = new TabbedPane
  val flightsTableModel = new FlightsTableModel
  val flightsTable = new Table {
    model = flightsTableModel
    selection.intervalMode = Table.IntervalMode.Single
    selection.elementMode = Table.ElementMode.Row

    override def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int): Component = {
      val c = super.rendererComponent(isSelected, focused, row, column)
      val editing = row == currentIdx  // always mark the currently edited flight
      if(!colored.selected) {
        if(editing || isSelected) {
          c.background = Colors.flightEditing
          c.foreground = selectionForeground
        } else {
          c.background = background
          c.foreground = foreground
        }
      } else {
        if(editing || isSelected) {
          c.background = Colors.flightEditing
          c.foreground = selectionForeground
        } else {
          if(!flightsTableModel.get(row).isValid) {
            c.background = Colors.colInvalid
          } else if(flightsTableModel.get(row).hasProblems) {
            c.background = Colors.colProblematic
          } else if(!flightsTableModel.get(row).finished) {
            c.background = Colors.colUnfinished
          } else {
            c.background = Colors.col
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
  colored.selected = true
  val btNew = new Button(I18n("new"))
  btNew.mnemonic = scala.swing.event.Key.N
  btNew.peer.setDisplayedMnemonicIndex(0)
  val btCopy = new Button(I18n("copy")) {
    enabled = false
  }
  val flightsPanel = new MigPanel("ins 0, fill", "", "[grow 0][fill]") {
    add(btNew)
    add(btCopy)
    add(unfinishedOnly)
    add(problemsOnly)
    add(colored, "wrap")
    add(new ScrollPane(flightsTable), "gap 0, span, w 100%")
  }

  val defaultsPanel = new DefaultsView
  val syncPanel = new SyncView

  tabs.pages += new TabbedPane.Page(I18n("flight"), flightPanel)
  tabs.pages += new TabbedPane.Page(I18n("defaults"), defaultsPanel)
  tabs.pages += new TabbedPane.Page(I18n("sync"), syncPanel)

  val split = new SplitPane {
    dividerSize = 12
    orientation = Orientation.Horizontal
    rightComponent = flightsPanel
    leftComponent = tabs
  }
  contents = split

  private var _enabled = true
  def enabled = _enabled
  def enabled_=(b : Boolean) = {
    _enabled = b
    btNew.enabled = b
    btCopy.enabled = b && flightsTable.selection.rows.size > 0
    unfinishedOnly.enabled = b
    problemsOnly.enabled = b
    colored.enabled = b
    flightsTable.enabled = b
    tabs.enabled = b
  }

  val exitAction = new AbstractAction {
    def actionPerformed(evt : ActionEvent) {
      close
      sys.exit(0)
    }
  }
  split.peer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Q"), "quit")
  split.peer.getActionMap().put("quit", exitAction);
}

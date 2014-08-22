package fmclient.views

import java.awt.event.{FocusEvent, FocusListener, KeyEvent, KeyListener, MouseEvent, MouseListener}
import javax.swing.event.{DocumentEvent, DocumentListener}
import java.awt.{Color, Dimension, Point, Container}
import scala.math.{min, max}
import scala.swing.event.Event
import scala.swing.Publisher
import fmclient.models.DefaultAutoCompleterModel.CreateOption
import fmclient.models.I18n
import java.util.regex.Pattern
import scala.util.matching.{ Regex => SRegex }
import javax.swing.{JTextField, JPopupMenu, JLayeredPane, JScrollPane,
                    DefaultListModel, DefaultListCellRenderer, SwingUtilities,
                    ListCellRenderer, ScrollPaneConstants, JList, JLabel,
                    ListSelectionModel}

object AutoCompleter {
  trait AutoCompleterModel[T >: Null <: AnyRef] {
    def filterString : String
    def filterString_=(s:String)
    def filteredOptions : Seq[Option[T]]

  }

  class Option[T >: Null <: AnyRef] {
    def get : T = null
    def toStringForList = toString
    def toStringForTextfield = toString
    override def toString = ""
  }

  class NilOption[T >: Null <: AnyRef] extends AutoCompleter.SyntheticOption[T] {
    override def toStringForList = s"[${I18n("nobody")}]"
    override def toStringForTextfield = ""
    override def matches(p : Pattern) = p.matcher(toStringForList.toLowerCase).find
    override def equals(o:Any) = {
      o.isInstanceOf[NilOption[T]]
    }
  }

  class RealOption[T >: Null <: AnyRef](e : T) extends AutoCompleter.Option[T] {
    if(e == null)
      throw new IllegalArgumentException("e must not be null")
    override def get = e
    override def toString() = e.toString
    override def equals(o:Any) = {
      if(o.isInstanceOf[RealOption[T]]) {
        o.asInstanceOf[RealOption[T]].get.equals(e)
      } else {
        false
      }
    }
  }

  /**
   * Marker trait for options returned
   * by a AutoCompleterModel that should
   * not be rendered by the ItemRenderer.
   * toString is called on them instead.
   */
  trait SyntheticOption[T >: Null <: AnyRef] extends AutoCompleter.Option[T] {
    def matches(p : Pattern) = false
  }

  class MyRegex(r: String) extends SRegex("") {
    override val pattern = Pattern.compile(r, Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
  }

  trait AutoCompleterItemRenderer[T >: Null <: AnyRef] {
    def html(str: String) = "<html>" + str + "</html>"
    def mark(str: String, replace : String) = {
      new MyRegex(replace).replaceAllIn(str, o => {
        "<u>" + str.substring(o.start, o.end) + "</u>"
      })
    }
    def renderForTextfield(o : AutoCompleter.Option[T]) : String
    def renderForList(o : AutoCompleter.Option[T], currentText : String) : String
  }

  class DefaultItemRenderer[T >: Null <: AnyRef] extends AutoCompleterItemRenderer[T] {
    override def renderForTextfield(o : AutoCompleter.Option[T]) = {
      if(o != null)
        o.toStringForTextfield
      else
        ""
    }

    override def renderForList(o : AutoCompleter.Option[T], replace : String) = {
      html(mark(o.toStringForList, replace))
    }
  }
  case class CreateEvent[T >: Null <: AnyRef](component : AutoCompleter[T], str : String, old : AutoCompleter.Option[T]) extends Event
  case class FocusLostEvent extends Event
  case class SelectionChangedEvent[T >: Null <: AnyRef](before : AutoCompleter.Option[T], after : AutoCompleter.Option[T]) extends Event
}


class MyCellRenderer[T >: Null <: AnyRef](itemRenderer : AutoCompleter.AutoCompleterItemRenderer[T])
  extends JLabel with ListCellRenderer[AutoCompleter.Option[T]] {
  private var txt = ""

  setOpaque(true)

  override def getListCellRendererComponent(list : JList[_ <: AutoCompleter.Option[T]], value : AutoCompleter.Option[T], i : Int, selected : Boolean, cellHasFocus : Boolean) : java.awt.Component = {
    if(selected)
      setBackground(Colors.labelBgSelected)
    else
      setBackground(Colors.labelBg)
    setText(itemRenderer.renderForList(value.asInstanceOf[AutoCompleter.Option[T]], txt))
    this
  }

  def updateText(str : String) = { txt = str }
}

/**
 * Documentation is for the weak and timid ;-)
 *
 * @author moser
 */
class AutoCompleter[T >: Null <: AnyRef](model : AutoCompleter.AutoCompleterModel[T],
  itemRenderer : AutoCompleter.AutoCompleterItemRenderer[T])
  extends JTextField with FocusListener with KeyListener with MouseListener with DocumentListener with Publisher {
  addKeyListener(this)
  addFocusListener(this)
  getDocument().addDocumentListener(this)
  setSize(new Dimension(20, 100))
  private val popupListModel = new DefaultListModel[AutoCompleter.Option[T]]
  private val popupList = new JList[AutoCompleter.Option[T]](popupListModel)
  private val cellRenderer = new MyCellRenderer[T](itemRenderer)
  popupList.setCellRenderer(cellRenderer)
  private val popup = new JPopupMenu();
  popup.setPopupSize(new Dimension(120, 80))
  val popupScrollPane = new JScrollPane(popupList)
  popupScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED)
  popup.add(popupScrollPane);
  popup.setBorderPainted(false)
  popupList.addMouseListener(this)
  popupList.addKeyListener(this)
  popupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
  popupList.setFocusable(false)
  private var topLevelContainer : JLayeredPane = null



  protected var pSelectedOption: AutoCompleter.Option[T] = new AutoCompleter.NilOption
  protected var pLastSelectedOption : AutoCompleter.Option[T] = _
  private var previousText = ""

  def this(m : AutoCompleter.AutoCompleterModel[T]) {
    this(m, new AutoCompleter.DefaultItemRenderer[T])
  }

  private val H = 150
  private val KEYS_UP = List(KeyEvent.VK_UP, KeyEvent.VK_PAGE_UP)
  private val KEYS_DOWN = List(KeyEvent.VK_DOWN, KeyEvent.VK_PAGE_DOWN)
  private val KEYS_ACCEPT = List(KeyEvent.VK_ENTER)
  private val KEYS_CANCEL = List(KeyEvent.VK_ESCAPE)
  private val KEYS_NO_FILTER = List(KeyEvent.VK_UP, KeyEvent.VK_PAGE_UP, KeyEvent.VK_DOWN,
          KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_HOME, KeyEvent.VK_END,
          KeyEvent.VK_ENTER, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
          KeyEvent.VK_KP_LEFT, KeyEvent.VK_KP_RIGHT)

  private def showPopup {
    val l = SwingUtilities.convertPoint(this, popup.getLocation(), popup)
    popup.setLocation(l.x, l.y + getHeight())
    popup.setVisible(true)
    updatePopup
  }

  private def updatePopup {
    if (!popup.isVisible) {
      showPopup
    } else {
      popupListModel.removeAllElements
      var i = 0
      model.filteredOptions.foreach(o => {
        popupListModel.add(i, o)
        i = i + 1
      })
      val add = if(popupList.getPreferredSize().getHeight() > H) 35 else 25
      popup.setPopupSize(max(getWidth(), popupList.getPreferredSize().getWidth()).toInt + add, min(popupList.getPreferredSize().getHeight() + 18, H).toInt)
      popupList.setSelectedValue(pSelectedOption, true)
      if(popupList.getSelectedIndex() == -1 && popupListModel.getSize() > 0) popupList.setSelectedIndex(0);
    }
  }

  private def hidePopup {
    if(popup != null) popup.setVisible(false) 
  }

  def reset {
    selectedOption = pSelectedOption
  }

  def revertLast {
    selectedOption = pLastSelectedOption
  }

  private def moveSelected(d : Int) {
    var i = popupList.getSelectedIndex() + d
    if (i < 0)
      i = popupListModel.getSize() + i
    if (i >= popupListModel.getSize())
      i = i % popupListModel.getSize()
    popupList.setSelectedValue(popupListModel.elementAt(i), true)
  }

  private def selectCurrentElement {
    if(popupList.getSelectedValue != null) selectedOption = popupList.getSelectedValue.asInstanceOf[AutoCompleter.Option[T]]
  }

  def listCellRenderer_=(l : ListCellRenderer[AutoCompleter.Option[T]]) = popupList.setCellRenderer(l)


  def selectedOption : AutoCompleter.Option[T] = pSelectedOption
  def selectedOption_=(obj : T) {
    selectedOption = if(obj != null) new AutoCompleter.RealOption[T](obj) else new AutoCompleter.NilOption[T]()
  }
  def selectedOption_=(opt : AutoCompleter.Option[T]) {
    if(pSelectedOption.isInstanceOf[AutoCompleter.RealOption[T]]) {
      pLastSelectedOption = pSelectedOption
    }
    pSelectedOption = opt
    setText(itemRenderer.renderForTextfield(opt))
    setCaretPosition(0)
    if(opt.isInstanceOf[CreateOption[T]]) {
      publish(AutoCompleter.CreateEvent[T](this, opt.asInstanceOf[CreateOption[T]].filterString, pLastSelectedOption))
    } else {
      publish(AutoCompleter.SelectionChangedEvent(pLastSelectedOption, opt))
    }
  }

  override def focusLost(e : FocusEvent) {
    if(popup.isVisible) {
      selectCurrentElement
      hidePopup
    }
    setCaretPosition(0)
    publish(AutoCompleter.FocusLostEvent())
  }

  override def focusGained(e : FocusEvent) {
    selectAll
    updatePopup
  }

  private def updateFilter {
    if (!getText.equals(previousText)) {
      previousText = getText
      model.filterString = previousText
      if(hasFocus) updatePopup
    }
  }

  override def insertUpdate(arg0 : DocumentEvent) {
    updateFilter
  }

  override def removeUpdate(arg0 : DocumentEvent) {
    updateFilter
  }

  override def keyReleased(e : KeyEvent) {
    if (this == e.getSource()) {
      if (KEYS_UP.contains(e.getKeyCode())) {
        moveSelected(-1)
      } else if (KEYS_DOWN.contains(e.getKeyCode())) {
        moveSelected(1)
      }
    }
    if (KEYS_ACCEPT.contains(e.getKeyCode())) {
      selectCurrentElement
      hidePopup
    } else if (KEYS_CANCEL.contains(e.getKeyCode())) {
      reset
      hidePopup
    }
    cellRenderer.updateText(getText)
  }

  override def mouseClicked(e : MouseEvent) {
    if (e.getClickCount() == 2) {
      selectCurrentElement
      hidePopup
    }
  }
  //override def enabled : Boolean = isEnabled()
  //override def enabled_=(b : Boolean) = setEnabled(b)

// Unused stuff
  override def keyPressed(arg0 : KeyEvent) {}
  override def keyTyped(arg0 : KeyEvent) {}

  override def mouseEntered(e : MouseEvent) {}
  override def mouseExited(e : MouseEvent) {}
  override def mousePressed(e : MouseEvent) {}
  override def mouseReleased(e : MouseEvent) {}

  override def changedUpdate(arg0 : DocumentEvent) {}
}


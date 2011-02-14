package de.moserei.foxtrotmike.client.views

import java.awt.event.{FocusEvent, FocusListener, KeyEvent, KeyListener, MouseEvent, MouseListener}
import javax.swing.event.{DocumentEvent, DocumentListener}
import java.awt.{Color, Dimension, Point, Container}
import scala.math.{min, max}
import javax.swing._

object AutoCompleter {
  trait AutoCompleterModel[T >: Null <: AnyRef] {
    protected var pFilterString : String = ""
    protected var pSelectedItem : AutoCompleter.Option[T] = new AutoCompleter.NilOption
    def filterString = pFilterString
    def filterString_=(s:String) = pFilterString = s
		def selectedItem = pSelectedItem
    def selectedItem_= = pSelectedItem = _:AutoCompleter.Option[T]
		def filteredItems : Seq[Option[T]]
		def forceUpdate : Unit
	}

  class Option[T >: Null <: AnyRef] {
    def get : T = null
    def toStringForList() = ""
    def toStringForTextfield() = ""
  }

  class NilOption[T >: Null <: AnyRef] extends AutoCompleter.SyntheticOption[T] {
    override def toStringForList = "-"
    override def toStringForTextfield = ""
    override def equals(o:Any) = {
      o.isInstanceOf[NilOption[T]]
    }
  }

  class RealOption[T >: Null <: AnyRef](e : T) extends AutoCompleter.Option[T] {
    if(e == null)
      throw new IllegalArgumentException("e must not be null")
    override def get = e
    override def toString() = e.toString
    override def toStringForList = toString
    override def toStringForTextfield = toString
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
	}

	trait AutoCompleterItemRenderer[T >: Null <: AnyRef] {
		def renderForTextfield(o : AutoCompleter.Option[T]) : String
    def renderForList(o : AutoCompleter.Option[T]) : String
	}

	class DefaultItemRenderer[T >: Null <: AnyRef] extends AutoCompleterItemRenderer[T] {
		override def renderForTextfield(o : AutoCompleter.Option[T]) = {
			o.toStringForTextfield
		}

    override def renderForList(o : AutoCompleter.Option[T]) = {
			o.toStringForList
		}
	}
}

/**
 * Documentation is for the weak and timid ;-)
 * 
 * @author moser
 */
class AutoCompleter[T >: Null <: AnyRef](model : AutoCompleter.AutoCompleterModel[T], itemRenderer : AutoCompleter.AutoCompleterItemRenderer[T]) extends JTextField with FocusListener with KeyListener with MouseListener with DocumentListener {
  addKeyListener(this)
	addFocusListener(this)
	getDocument().addDocumentListener(this)
	setSize(new Dimension(20, 100))
  private val popupListModel = new DefaultListModel
	private val popupList = new JList(popupListModel)
  popupList.setCellRenderer(new DefaultListCellRenderer() {
    override def getListCellRendererComponent(list : JList,  value : Any, i : Int, selected : Boolean, cellHasFocus : Boolean) = {
      val l = super.getListCellRendererComponent(list, value, i, selected, cellHasFocus).asInstanceOf[JLabel]
      l.setText(itemRenderer.renderForList(value.asInstanceOf[AutoCompleter.Option[T]]))
      l
    }
  })
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
			model.filteredItems.foreach(o => {
				popupListModel.add(i, o)
        i = i + 1
			})
			popupList.setSelectedValue(pSelectedItem, true)
			if(popupList.getSelectedIndex() == -1 && popupListModel.getSize() > 0) popupList.setSelectedIndex(0);
			val add = if(popupList.getPreferredSize().getHeight() > H) 35 else 15
			popup.setPopupSize(max(getWidth(), popupList.getPreferredSize().getWidth()).toInt + add, min(popupList.getPreferredSize().getHeight() + 18, H).toInt)
		}
	}

	private def hidePopup {
		if(popup != null) popup.setVisible(false) 
	}

	private def reset {
		pSelectedItem = pSelectedItem
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
    if(popupList.getSelectedValue != null) pSelectedItem = popupList.getSelectedValue.asInstanceOf[AutoCompleter.Option[T]]
	}

	def listCellRenderer_=(l : ListCellRenderer) = popupList.setCellRenderer(l)

  private def pSelectedItem = model.selectedItem

	private def pSelectedItem_=(o : AutoCompleter.Option[T]) {
		model.selectedItem = o
		setText(itemRenderer.renderForTextfield(o))
		setCaretPosition(0)
	}

	def selectedItem : T = pSelectedItem.get

  def selectedItem_=(o : T) {
    pSelectedItem = if(o != null) new AutoCompleter.RealOption(o) else new AutoCompleter.NilOption()
  }

	override def focusLost(e : FocusEvent) {
		if(popup.isVisible) {
			selectCurrentElement
			hidePopup
		}
		setCaretPosition(0)
	}

	override def focusGained(e : FocusEvent) {
		selectAll
		model.forceUpdate
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
	}

	override def mouseClicked(e : MouseEvent) {
		if (e.getClickCount() == 2) {
			selectCurrentElement
			hidePopup
		}
	}

// Unused stuff
	override def keyPressed(arg0 : KeyEvent) {}	
  override def keyTyped(arg0 : KeyEvent) {}

	override def mouseEntered(e : MouseEvent) {}
	override def mouseExited(e : MouseEvent) {}
	override def mousePressed(e : MouseEvent) {}
	override def mouseReleased(e : MouseEvent) {}

	override def changedUpdate(arg0 : DocumentEvent) {}
}


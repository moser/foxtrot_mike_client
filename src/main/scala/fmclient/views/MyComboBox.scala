package fmclient.views

import swing.event._
import javax.swing.{JList, JComponent, JComboBox, JTextField, ComboBoxModel, AbstractListModel, ListCellRenderer}
import swing._

class MyComboBox[T](var model : ComboBoxModel[T]) extends Component with Publisher {
  override lazy val peer: JComboBox[T] = new JComboBox[T](model) with SuperMixin

  object selection extends Publisher {
    def index: Int = peer.getSelectedIndex
    def index_=(n: Int) { peer.setSelectedIndex(n) }
    def item: T = peer.getSelectedItem.asInstanceOf[T]
    def item_=(a: T) { peer.setSelectedItem(a) }

    peer.addActionListener(Swing.ActionListener { e =>
      publish(event.SelectionChanged(MyComboBox.this))
    })
  }
}

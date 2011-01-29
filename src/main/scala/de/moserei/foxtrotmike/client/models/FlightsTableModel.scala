package de.moserei.foxtrotmike.client.models

import javax.swing.table.AbstractTableModel
import java.text.DateFormat
import org.joda.time.DateTime
import repos.AllFlights

class FlightsTableModel extends AbstractTableModel {
  private var pUnfinishedOnly = false
  val dateFormatter = DateFormat.getDateInstance
  val timeFormatter = TimeFormatterFactory.getFormatter(null)
  def int2Object(o: Int): Object = o.asInstanceOf[AnyRef]
  val cols : List[(String, Flight => Object)] = List(("departure_date", { f : Flight => dateFormatter.format(f.departureDate) }),
                  ("plane", { f: Flight => f.plane }),
                  ("seat1", { f: Flight => f.seat1 }),
                  ("seat2", { f: Flight => f.seat2 }),
                  ("from", { f: Flight => f.from }),
                  ("to", { f: Flight => f.to }),
                  ("departure_time", { f: Flight => timeFormatter.valueToString(int2Object(f.departureTime)) }),
                  ("arrival_time", { f: Flight => timeFormatter.valueToString(int2Object(f.arrivalTime)) }),
                  ("duration", { f: Flight => f.durationString }))
  var all : Seq[Flight] = Nil
  var update_all = true
  FlightsTableModels.addInstance(this)
    
  override def getRowCount = {
    getAll.length
  }
  
  override def getColumnCount = {
    cols.length
  }
  
  override def getColumnName(i: Int) = {
    I18n(cols(i)._1)
  }
  
  override def getColumnClass(i: Int) = classOf[String]
  
  override def getValueAt(r: Int, c: Int): Object = { 
    cols(c)._2(getAll(r))
  }
  
  override def isCellEditable(r: Int, c: Int): Boolean = false

  def getAll = {
    if(update_all) {
      def departureTimeX(f:Flight) = if(f.departureTime > 0) f.departureTime else 1441
      def dt(f:Flight) = new DateTime(f.departureDate)
      all = AllFlights.all.sortWith((f1, f2) => dt(f1).compareTo(dt(f2)) == -1 || (dt(f1).compareTo(dt(f2)) == 0 && departureTimeX(f1) < departureTimeX(f2))).reverse
      if(pUnfinishedOnly) all = all.filter(!_.finished) 
      update_all = false
    }
    all
  }

  def indexOf(f : Flight) = getAll.indexOf(f) 

  def insert(f : Flight) = {
    update_all = true
    val i = indexOf(f)
    fireTableRowsInserted(i, i)
  }

  def update(f : Flight) = {
    val i = indexOf(f) //index before update
    update_all = true
    val j = indexOf(f)
    fireTableRowsUpdated(i, j)
  }
  
  def remove(f : Flight) {
    val i = indexOf(f)
    update_all = true
    fireTableRowsDeleted(i, i)
  }
  
  def unfinishedOnly = pUnfinishedOnly
  def unfinishedOnly_=(t : Boolean) = {
    pUnfinishedOnly = t
    update_all = true
    fireTableDataChanged()
  } 
}

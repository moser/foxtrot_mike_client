package de.moserei.foxtrotmike.client.models

import javax.swing.text._
import javax.swing.JFormattedTextField.AbstractFormatterFactory
import javax.swing.JFormattedTextField

class LessSuckingTimeFormatter extends DefaultFormatter {
  setCommitsOnValidEdit(true)
  setOverwriteMode(false)
  implicit def string2Int(s: String): Int = augmentString(s).toInt
  implicit def object2Int(o: Object): Int = o.asInstanceOf[Int]
  def int2Object(o: Int): Object = o.asInstanceOf[AnyRef]

  val withPoint = """(\d{1,2})\.(\d{1,2})""".r
  val withColon = """(\d{1,2}):(\d{1,2})""".r
  val withoutColon = """(\d{1,2})(\d{2})""".r
//  val documentFilter = new TimeDocumentFilter
//  val navigationFilter = new TimeNavigationFilter
//  override def getDocumentFilter = documentFilter
//  override def getNavigationFilter = navigationFilter

  override def stringToValue(s : String) : Object = {
    s match {
      case withPoint(h,m) => int2Object((h % 24) * 60 + (m % 60))
      case withColon(h,m) => int2Object((h % 24) * 60 + (m % 60))
      case withoutColon(h,m) => int2Object((h % 24) * 60 + (m % 60))
      case _ => int2Object(-1)
    }
  }

  override def valueToString(o : Object) : String = {
    if(o.isInstanceOf[Int] && o != -1)
      "%02d:%02d".format(o / 60, o % 60)
    else
      ""
  }
}

object TimeFormatterFactory extends AbstractFormatterFactory {
  def getFormatter(f : JFormattedTextField) = new LessSuckingTimeFormatter
}

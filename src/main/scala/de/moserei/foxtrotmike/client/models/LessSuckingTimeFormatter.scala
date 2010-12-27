package de.moserei.foxtrotmike.client.models

import javax.swing.text._
import javax.swing.JFormattedTextField.AbstractFormatterFactory
import javax.swing.JFormattedTextField

class LessSuckingTimeFormatter extends DefaultFormatter {
  setCommitsOnValidEdit(true)
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

  class TimeDocumentFilter extends DocumentFilter {
    override def insertString(fb : DocumentFilter.FilterBypass, offset : Int, str : String, attr : AttributeSet) {}
    override def replace(fb : DocumentFilter.FilterBypass, offset : Int, length : Int, str : String, attr : AttributeSet) {
      if(offset != fb.getDocument.getLength)
        fb.replace(offset, str.length, str, attr)
    }
    override def remove(fb : DocumentFilter.FilterBypass, offset : Int, length : Int) {}
  }

  class TimeNavigationFilter extends NavigationFilter {
    override def setDot(fb: NavigationFilter.FilterBypass, offset : Int, bias : Position.Bias) {
      if(offset == 2)
        super.setDot(fb, offset + (offset - fb.getCaret.getDot), bias)
      else
        super.setDot(fb, offset, bias)
    }

    override def moveDot(fb: NavigationFilter.FilterBypass, dot : Int, bias : Position.Bias) {}
  }
}

object TimeFormatterFactory extends AbstractFormatterFactory {
  def getFormatter(f : JFormattedTextField) = new LessSuckingTimeFormatter
}
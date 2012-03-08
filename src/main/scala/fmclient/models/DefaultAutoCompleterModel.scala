package fmclient.models

import fmclient.views.AutoCompleter
import fmclient.models.repos.BaseEntityRepository
import scala.swing.Publisher
import java.util.regex.Pattern

object DefaultAutoCompleterModel {
  class CreateOption[T >: Null <: AnyRef](val filterString : String) extends AutoCompleter.SyntheticOption[T] {
    override def toString = filterString
    override def toStringForTextfield = filterString
    override def toStringForList = I18n("create", Array(filterString))
  }
}

class DefaultAutoCompleterModel[T >: Null <: BaseModel[_]](collection : BaseEntityRepository[T, _], extract : T => String, options_ : Map[String, Boolean] = Map()) extends AutoCompleter.AutoCompleterModel[T] with Publisher {
  val options = Map("allowNil" -> true, "allowCreate" -> true) ++ options_
  var dirty = true
  var pFilteredOptions: Seq[AutoCompleter.Option[T]] = List()

//  override def filterString = filterString_
  override def filterString_=(s:String) = {
    pFilterString = s
    dirty = true
  }

  def syntheticOptions : Seq[AutoCompleter.SyntheticOption[T]] = List()

  def extractMatching(p : Pattern, s: Seq[T]) = {
    var n = 0
    var i = s.toIterator
    var r = Seq[T]()
    while(i.hasNext && n < 15) {
      var e = i.next
      if(p.matcher(extract(e).toLowerCase).find) {
        r = r :+ e
        n += 1
      }
    }
    r.map(new AutoCompleter.RealOption[T](_))
  }

  def extractMatchingFromCollection(p : Pattern) = {
    extractMatching(p, collection.all)
  }

  override def filteredOptions = {
    if(dirty) {
      val p = Pattern.compile(filterString.toLowerCase, Pattern.LITERAL)
      pFilteredOptions = extractMatchingFromCollection(p)
      pFilteredOptions = pFilteredOptions ++ syntheticOptions.filter(_.matches(p))
      if(options("allowCreate") && pFilteredOptions.length == 0 &&
        !(selectedOption.isInstanceOf[AutoCompleter.NilOption[T]] &&
        filterString.equals(selectedOption.toString))) {
        pFilteredOptions = pFilteredOptions ++ List(new DefaultAutoCompleterModel.CreateOption[T](filterString).asInstanceOf[AutoCompleter.Option[T]])
      }
      if(filterString.equals("") && options("allowNil")) {
        pFilteredOptions = List(new AutoCompleter.NilOption[T].asInstanceOf[AutoCompleter.Option[T]]) ++ pFilteredOptions
      }
      dirty = false
    }
    pFilteredOptions
  }

  override def forceUpdate {
    dirty = true
  }
}

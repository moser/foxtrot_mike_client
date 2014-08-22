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
  val nilOption = new AutoCompleter.NilOption[T]

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

  override def filteredOptions(search : String) = {
    val p = Pattern.compile(search.toLowerCase, Pattern.LITERAL)
    var result : Seq[AutoCompleter.Option[T]] = extractMatchingFromCollection(p)
    result = result ++ syntheticOptions.filter(_.matches(p))
    if(options("allowCreate") && result.length == 0)
      result = result ++ List(new DefaultAutoCompleterModel.CreateOption[T](search).asInstanceOf[AutoCompleter.Option[T]])
    if((search.equals("") || nilOption.matches(p)) && options("allowNil"))
      result = List(nilOption) ++ result
    result
  }
}

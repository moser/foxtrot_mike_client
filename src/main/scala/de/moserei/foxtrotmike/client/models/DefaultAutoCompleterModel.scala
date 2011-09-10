package de.moserei.foxtrotmike.client.models

import de.moserei.foxtrotmike.client.views.AutoCompleter
import de.moserei.foxtrotmike.client.models.repos.BaseEntityRepository
import scala.util.matching.Regex
import scala.swing.Publisher

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
  var pFilteredItems : Seq[AutoCompleter.Option[T]] = List()

//  override def filterString = filterString_
  override def filterString_=(s:String) = {
    pFilterString = s
    dirty = true
  }

  override def filteredItems = {
    if(dirty) {
      val r = new Regex(filterString.toLowerCase)
      pFilteredItems = collection.all.filter(o => { r.findFirstIn(extract(o).toLowerCase) != None }).map(new AutoCompleter.RealOption[T](_))
      if(options("allowCreate") && pFilteredItems.length == 0 && !(selectedItem.isInstanceOf[AutoCompleter.NilOption[T]] && filterString.equals(selectedItem.toString))) //&& !(@selected_item.is_a?(NilOption) && @filter_string == @selected_item.to_s))
        pFilteredItems = pFilteredItems ++ List(new DefaultAutoCompleterModel.CreateOption[T](filterString).asInstanceOf[AutoCompleter.Option[T]])
      if(options("allowNil") && pFilteredItems.findIndexOf(_.isInstanceOf[AutoCompleter.NilOption[T]]) == -1)
        pFilteredItems = List(new AutoCompleter.NilOption[T].asInstanceOf[AutoCompleter.Option[T]]) ++ pFilteredItems
      dirty = false
    }
    pFilteredItems
  }

  override def forceUpdate {
    dirty = true
  }
}

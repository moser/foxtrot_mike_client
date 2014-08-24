package fmclient.models

import fmclient.views.AutoCompleter
import fmclient.models.repos.EnabledAndDisabledStringIndexedEntityRepository
import scala.swing.Publisher
import java.util.regex.Pattern

class EnabledFirstAutoCompleterModel[T >: Null <: BaseModel[String]](
  collection : EnabledAndDisabledStringIndexedEntityRepository[T],
  extract : T => String,
  options_ : Map[String, Boolean] = Map())
extends DefaultAutoCompleterModel[T](collection, extract, options_) {
  override def sortOptions(seq : Seq[AutoCompleter.RealOption[T]]) = {    
    seq.sortBy((e) => (e.get.disabled, e.get.sortingKey))
  }
}

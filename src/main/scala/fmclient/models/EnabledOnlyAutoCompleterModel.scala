package fmclient.models

import fmclient.views.AutoCompleter
import fmclient.models.repos.EnabledAndDisabledStringIndexedEntityRepository
import scala.swing.Publisher
import java.util.regex.Pattern

class EnabledOnlyAutoCompleterModel[T >: Null <: BaseModel[String]](collection : EnabledAndDisabledStringIndexedEntityRepository[T], extract : T => String, options_ : Map[String, Boolean] = Map()) extends DefaultAutoCompleterModel[T](collection, extract, options_) {

  override def extractMatchingFromCollection(p : Pattern) = {
    var r = extractMatching(p, collection.enabled)
    if(r.length < 1)
      r = r ++ extractMatching(p, collection.disabled)
    r
  }
}

// vim:set ts=2 sw=2:
package fmclient.models

import fmclient.views.AutoCompleter
import fmclient.models.repos.AllPeople
import java.util.regex.Pattern

object Seat1ACModel {
  class UnknownPersonOption extends AutoCompleter.SyntheticOption[Person] {
    override def matches(p : Pattern) = {
      p.matcher(toString.toLowerCase).find
    }
    override def toString = I18n("unknown_person")
    override def equals(o:Any) = {
      o.isInstanceOf[UnknownPersonOption]
    }
  }
}

class Seat1ACModel extends DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false)) {
  override def syntheticOptions = List(new Seat1ACModel.UnknownPersonOption)
}

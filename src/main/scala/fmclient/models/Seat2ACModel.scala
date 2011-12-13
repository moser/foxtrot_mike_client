// vim:set ts=2 sw=2:
package fmclient.models

import fmclient.views.AutoCompleter
import fmclient.models.repos.AllPeople
import java.util.regex.Pattern

object Seat2ACModel {
  class NumberOption(val n : Integer) extends AutoCompleter.SyntheticOption[Person] {
    override def matches(p : Pattern) = {
      p.matcher(toString).find
    }
    override def toString = "+" + n
    override def equals(o : Any) = {
      if(o.isInstanceOf[NumberOption]) {
        o.asInstanceOf[NumberOption].n == n
      } else {
        false
      }
    }
  }
}

class Seat2ACModel extends DefaultAutoCompleterModel[Person](AllPeople, _.name) {
  override def syntheticOptions = List(new Seat2ACModel.NumberOption(1),
                                       new Seat2ACModel.NumberOption(2),
                                       new Seat2ACModel.NumberOption(3))
}

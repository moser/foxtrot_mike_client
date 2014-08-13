// vim:set ts=2 sw=2:
package fmclient.models

import fmclient.views.AutoCompleter
import fmclient.models.repos.AllPeople
import java.util.regex.Pattern

class Seat1ACModel extends EnabledOnlyAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false))

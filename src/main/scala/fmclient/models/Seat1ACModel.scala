// vim:set ts=2 sw=2:
package fmclient.models

import fmclient.views.AutoCompleter
import fmclient.models.repos.AllPeople
import java.util.regex.Pattern

class Seat1ACModel(flightGetter : () => AbstractFlight)
extends EnabledFirstAutoCompleterModel[Person](
  AllPeople,
  _.name,
  Map("allowNil" -> false)) {
  override def sortOptions(seq : Seq[AutoCompleter.RealOption[Person]]) = {
    val flight = flightGetter()
    if(flight.plane == null || flight.plane.legalPlaneClass == null) {
      seq.sortBy((e) => (e.get.disabled, e.get.sortingKey))
    } else {
      val group = flight.plane.group.id
      val legalPlaneClass = flight.plane.legalPlaneClass.id
      val seats = flight.plane.seatCount
      if(seats > 1) {
        seq.sortBy((e) => (e.get.disabled,
                           (e.get.group.id - group).abs,
                           -e.get.licenseLevel(legalPlaneClass, -1),
                           e.get.sortingKey))
      } else {
        seq.sortBy((e) => (e.get.disabled,
                           (e.get.group.id - group).abs,
                           e.get.licenseLevel(legalPlaneClass, 100) == 100,
                           e.get.sortingKey))
      }
    }
  }
}

package de.moserei.foxtrotmike.client.models

import javax.persistence._

@Entity
@DiscriminatorValue("F")
class Flight extends AbstractFlight {
  override def afterSave(create : Boolean) {
    if(!create) {
      FlightsTableModels.update(this)
    } else {
      FlightsTableModels.insert(this)
    }
  }

  def this(d : Defaults) {
    this()
    from = d.airfield
    to = d.airfield
    controller = d.controller
    departureDate = d.date
  }
}

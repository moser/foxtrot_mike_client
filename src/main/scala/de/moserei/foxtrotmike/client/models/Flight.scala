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
}

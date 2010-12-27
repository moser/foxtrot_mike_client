package de.moserei.foxtrotmike.client.models

import javax.persistence._

@Entity
@DiscriminatorValue("T")
class TowLaunch extends Launch {
  @OneToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="tow_flight_id")
  var towFlight : Flight = _
}
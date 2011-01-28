package de.moserei.foxtrotmike.client.models

import javax.persistence._

@Entity
@DiscriminatorValue("T")
class TowLaunch extends Launch {
  def this(flight0 : Flight) = {
    this()
    flight = flight0
    towFlight = new TowFlight(this)
  }
  
  def this(flight0 : Flight, d : Defaults) = {
    this(flight0)
    towFlight = new TowFlight(this, d)
  }
  
  @OneToOne(fetch=FetchType.EAGER, cascade = Array(CascadeType.ALL))
  @JoinColumn(name="tow_flight_id")
  var towFlight : TowFlight = _
}

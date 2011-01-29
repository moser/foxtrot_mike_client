package de.moserei.foxtrotmike.client.models

import javax.persistence._

@Entity
@DiscriminatorValue("W")
class WireLaunch extends Launch {
  def this(flight0 : Flight) = {
    this()
    flight = flight0
  }
  def this(flight0 : Flight, d : Defaults) {
    this(flight0)
    wireLauncher = d.wireLauncher
    operator = d.operator
  }
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="wire_launcher_id")
  var wireLauncher : WireLauncher = _
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="operator_id")
  var operator : Person = _
}

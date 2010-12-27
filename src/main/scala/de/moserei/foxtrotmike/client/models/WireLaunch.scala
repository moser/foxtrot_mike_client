package de.moserei.foxtrotmike.client.models

import javax.persistence._

@Entity
@DiscriminatorValue("W")
class WireLaunch extends Launch {
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="wire_launcher_id")
  var wireLauncher : WireLauncher = _
}
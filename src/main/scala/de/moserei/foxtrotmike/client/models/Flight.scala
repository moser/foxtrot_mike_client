package de.moserei.foxtrotmike.client.models

import javax.persistence._

@Entity
@DiscriminatorValue("F")
class Flight extends AbstractFlight {
  @OneToOne(fetch=FetchType.EAGER, mappedBy="flight", cascade = Array(CascadeType.ALL))
  var launch : Launch = _

  override def afterSave(create : Boolean) {
    if(!create) {
      FlightsTableModels.update(this)
    } else {
      FlightsTableModels.insert(this)
    }
  }
  
  override def afterDelete = {
    FlightsTableModels.remove(this)
  }

  def this(d : Defaults) {
    this()
    from = d.airfield
    to = d.airfield
    controller = d.controller
    departureDate = d.date
  }
  
  def launchType = {
    if(launch.isInstanceOf[WireLaunch]) {
      "WireLaunch"
    } else if(launch.isInstanceOf[TowLaunch]) {
      "TowLaunch"
    } else {
      "SelfLaunch"
    }
  }
  
  def launchType_=(s:String) = {
    if(launchType != s) {
      if(s == "SelfLaunch") {
        launch = null
      } else {
        if(s == "WireLaunch") {
          launch = new WireLaunch(this, DefaultsSingleton)
        } else {
          launch = new TowLaunch(this, DefaultsSingleton)
        }
      }
    }
  }
  
  def finished = {
    departureTime >= 0 && arrivalTime >= 0
  }
}

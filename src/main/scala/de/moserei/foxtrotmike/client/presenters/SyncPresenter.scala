package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.SyncView
import de.moserei.foxtrotmike.client.models.repos._
import swing.event._
import scala.actors.Actor._

class SyncPresenter {
  val view = new SyncView
  val down = List[BaseEntityRepository[_]](AllAirfields, AllPeople, AllPlanes, AllWireLaunchers)
  val up = List[BaseEntityRepository[_]](AllAirfields, AllPeople, AllPlanes, AllWireLaunchers)
  view.btDown.reactions += {
    case ButtonClicked(_) => {
      view.progress.value = 0
      var done = false
      var progressUpdater = actor {
        loopWhile(!done) {
          receive {
            case a : Double => view.progress.value += (100 / down.length * a).toInt
            case false => {
              view.progress.value = 100
              done = true
            }
          }
        }
      }
      var backgroundSync = actor {
        down.foreach(r => {
          r.syncDown(view.username.text, new String(view.password.password), progressUpdater)
        })
        progressUpdater ! false
      }
      progressUpdater.start
      backgroundSync.start
    }
  }
  
  view.btUp.reactions += {
    case ButtonClicked(_) => {
      view.progress.value = 0
      var done = false
      var progressUpdater = actor {
        loopWhile(!done) {
          receive {
            case a : Double => view.progress.value += (100 / up.length * a).toInt
            case false => {
              view.progress.value = 100
              done = true
            }
          }
        }
      }
      var backgroundSync = actor {
        up.foreach(r => {
          r.syncUp(view.username.text, new String(view.password.password), progressUpdater)
        })
        AllFlights.syncUp(view.username.text, new String(view.password.password), progressUpdater)
        AllFlights.all.filter(_.status == "synced").foreach(f => {
          f.delete
        })
        progressUpdater ! false
      }
      progressUpdater.start
      backgroundSync.start
    }
  }
  
  view.btNo.reactions += {
    case ButtonClicked(_) => {
      view.close
    }
  } 
}

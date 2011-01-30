package de.moserei.foxtrotmike.client.presenters

import de.moserei.foxtrotmike.client.views.SyncView
import de.moserei.foxtrotmike.client.models.repos._
import de.moserei.foxtrotmike.client.models.I18n
import swing.event._
import scala.actors.Actor._
import scala.swing.Dialog

class SyncPresenter {
  val view = new SyncView
  val down = List(AllAirfields.syncDown(_,_,_), AllPeople.syncDown(_,_,_), AllPlanes.syncDown(_,_,_), AllWireLaunchers.syncDown(_,_,_))
  val up = List(AllAirfields.syncUp(_,_,_), AllPeople.syncUp(_,_,_), AllPlanes.syncUp(_,_,_), AllWireLaunchers.syncUp(_,_,_))
  
  view.btDown.reactions += {
    case ButtonClicked(_) => {
      sync(false)
    }
  }
  
  view.btUp.reactions += {
    case ButtonClicked(_) => {
      sync(true)
    }
  }
  
  private def sync(dirUp : Boolean) = {
    view.progress.value = 0
    var progressUpdater = actor {
      var done = false
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
      var cont = true
      var s = down
      if(dirUp) s = up
      s.foreach(r => {
        if(cont)
          try {
            r(view.username.text, new String(view.password.password), progressUpdater)
          } catch {
            case e:org.apache.http.conn.HttpHostConnectException => {
              Dialog.showMessage(null, I18n("error.connection"), I18n("error"), Dialog.Message.Error)
              cont = false
            }
            case dispatch.StatusCode(401, _) => {
              Dialog.showMessage(null, I18n("error.access_denied"), I18n("error"), Dialog.Message.Error)
              cont = false
            }
            case e : Throwable => {
              Dialog.showMessage(null, I18n("error.unknown_error") + e.toString, I18n("error"), Dialog.Message.Error)
              cont = false
            }
          }
      })
      if(dirUp) {
        AllFlights.syncUp(view.username.text, new String(view.password.password), progressUpdater)
        AllFlights.all.filter(_.status == "synced").foreach(_.delete)
      }
      progressUpdater ! false
    }
    progressUpdater.start
    backgroundSync.start
  }
  
  view.btNo.reactions += {
    case ButtonClicked(_) => {
      view.close
    }
  } 
}

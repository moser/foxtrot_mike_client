package fmclient.presenters

import fmclient.views.SyncView
import fmclient.models.repos._
import fmclient.models.{I18n, Config, DefaultsSingleton}
import swing.event._
import scala.actors.Actor._
import scala.swing.Dialog

class SyncPresenter(view0 : SyncView, mp : MainPresenter) {
  val view = view0
  val down = List(AllGroups.syncDown(_,_,_), AllAirfields.syncDown(_,_,_), AllPeople.syncDown(_,_,_), AllPlanes.syncDown(_,_,_), AllWireLaunchers.syncDown(_,_,_), AllCostHints.syncDown(_,_,_), AllLegalPlaneClasses.syncDown(_,_,_))
  val up = List(AllGroups.syncUp(_,_,_), AllAirfields.syncUp(_,_,_), AllPeople.syncUp(_,_,_), AllPlanes.syncUp(_,_,_), AllWireLaunchers.syncUp(_,_,_))

  view.username.text = Config.lastUser

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
    Config.lastUser = view.username.text
    view.enabled = false
    view.progress.value = 0
    view.info.text = ""
    var s = down
    if(dirUp) s = up
    mp.view.enabled = false
    var progressUpdater = actor {
      var done = false
      loopWhile(!done) {
        receive {
          case SyncEvent(act, obj, prog) => {
            view.info.text = "" + act + " " + obj + "\n" + view.info.text
            view.progress.value += (100 / s.length * prog).toInt
          }
          case false => {
            view.progress.value = 100
            done = true
            view.enabled = true
          }
        }
      }
    }
    var backgroundSync = actor {
      try {
        s.foreach(r => r(view.username.text, new String(view.password.password), progressUpdater))
        if(dirUp) {
          AllFlights.syncUp(view.username.text, new String(view.password.password), progressUpdater)
          AllFlights.all.filter(_.status == "synced").foreach(_.delete)
          mp.selectFirstOrNull
        }
      } catch {
        case e:org.apache.http.conn.HttpHostConnectException => {
          Dialog.showMessage(null, I18n("error.connection"), I18n("error"), Dialog.Message.Error)
        }
        case dispatch.StatusCode(401, _) => {
          Dialog.showMessage(null, I18n("error.access_denied"), I18n("error"), Dialog.Message.Error)
        }
        case e : Throwable => {
          Dialog.showMessage(null, I18n("error.unknown_error") + e.getMessage().split("\n")(0), I18n("error"), Dialog.Message.Error)
          e.printStackTrace
        }
      }
      progressUpdater ! false
      DefaultsSingleton.update
      mp.view.enabled = true
    }
    progressUpdater.start
    backgroundSync.start
  }
}

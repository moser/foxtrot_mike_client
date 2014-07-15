package fmclient.presenters

import fmclient.views.SyncView
import fmclient.models.repos._
import fmclient.models.{I18n, Config, DefaultsSingleton, Print}
import fmclient.models.Print._
import swing.event._
import scala.swing.Dialog
import javax.swing.SwingUtilities
import java.util.Date

class SyncPresenter(view0 : SyncView, mp : MainPresenter) {
  val view = view0
  val down = List(AllGroups.syncDown(_,_,_), AllAirfields.syncDown(_,_,_), AllPeople.syncDown(_,_,_), AllPlanes.syncDown(_,_,_), AllWireLaunchers.syncDown(_,_,_), AllCostHints.syncDown(_,_,_), AllLegalPlaneClasses.syncDown(_,_,_))
  val up = List(AllGroups.syncUp(_,_,_), AllAirfields.syncUp(_,_,_), AllPeople.syncUp(_,_,_), AllPlanes.syncUp(_,_,_), AllWireLaunchers.syncUp(_,_,_), AllFlights.syncUp(_,_,_))

  view.username.text = Config.lastUser
  view.date.peer.setValue(DefaultsSingleton.date)
  view.airfield.selectedOption = DefaultsSingleton.airfield

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

  view.btPrint.reactions += {
    case ButtonClicked(_) => {
      try {
        Print.getPdf(view.username.text, new String(view.password.password), view.airfield.selectedOption.get, view.date.peer.getValue.asInstanceOf[Date])
      } catch {
        case HttpCode(_) => {
          Dialog.showMessage(null, I18n("error.connection"), I18n("error"), Dialog.Message.Error)
        }
      }
       
    }
  }

  class ProgressUpdater(repoCount : Int) {
    def sync(action : String, obj : String, progress : Double) {
      SwingUtilities.invokeLater(new Runnable {
        override def run {
          view.info.text = "" + action + " " + obj + "\n" + view.info.text
          view.progress.value += (100 / repoCount * progress).toInt
        }
      })
    }
    def done() {
      SwingUtilities.invokeLater(new Runnable {
        override def run {
          view.progress.value = 100
          view.enabled = true
        }
      })
    }
  }

  case class Credentials(username : String, password : String)

  class BackgroundUpdate(repos : List[(String, String, ((String, String, Double) => Unit)) => Unit], cred : Credentials) extends Runnable {
    override def run {
      val updater = new ProgressUpdater(repos.length)
      try {
        repos.foreach(repo => repo(cred.username, cred.password, updater.sync))
      } catch {
        case e : Throwable => {
          println(e)
          e.printStackTrace
          Dialog.showMessage(null, I18n("error.unknown_error") + e.getMessage().split("\n")(0), I18n("error"), Dialog.Message.Error)
        }
      } finally {
        updater.done()
        DefaultsSingleton.update
        AllFlights.all.filter(_.status == "synced").foreach(_.delete(false))
        mp.invalidateFTM
        SwingUtilities.invokeLater(new Runnable {
          override def run {
            mp.selectFirstOrNull
            mp.view.enabled = true
          }
        })
      }
    }
  }

  private def sync(dirUp : Boolean) = {
    Config.lastUser = view.username.text
    view.enabled = false
    view.progress.value = 0
    view.info.text = ""
    var sync = down
    if(dirUp) sync = up
    mp.view.enabled = false
    new Thread(new BackgroundUpdate(sync, Credentials(view.username.text, new String(view.password.password)))).start
  }
}

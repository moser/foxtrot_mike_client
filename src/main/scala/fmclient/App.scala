package fmclient

import fmclient.presenters.MainPresenter
import swing._
import org.joda.time.DateTimeZone
import java.util.TimeZone

object App extends SwingApplication {
  private var mainPresenter : MainPresenter = _
  def startup(args: Array[String]) {
    DateTimeZone.setDefault(DateTimeZone.UTC)
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")
    //javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
    mainPresenter = new MainPresenter
  }
}

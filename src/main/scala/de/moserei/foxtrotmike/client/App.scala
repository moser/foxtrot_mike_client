package de.moserei.foxtrotmike.client

import de.moserei.foxtrotmike.client.presenters.MainPresenter
import swing._
import org.scala_tools.time.Imports._

object App extends SwingApplication {
  private var mainPresenter : MainPresenter = _
	def startup(args: Array[String]) {
    DateTimeZone.setDefault(DateTimeZone.UTC)
    javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")
    //javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
    mainPresenter = new MainPresenter
  }
}

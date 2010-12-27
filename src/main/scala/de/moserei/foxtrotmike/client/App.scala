package de.moserei.foxtrotmike.client

import de.moserei.foxtrotmike.client.presenters.MainPresenter
import swing._
import de.moserei.foxtrotmike.client.models.EntityMgr
import org.scala_tools.time.Imports._

object App extends SwingApplication {
	lazy val top = new MainPresenter
	
	def startup(args: Array[String]) {
    DateTimeZone.setDefault(DateTimeZone.UTC)
    javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")
    top.nop
  }

  override def shutdown() {
    EntityMgr.close
  }
}

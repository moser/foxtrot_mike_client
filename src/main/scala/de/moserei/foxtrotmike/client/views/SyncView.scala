package de.moserei.foxtrotmike.client.views

import swing._
import de.moserei.foxtrotmike.client.models.I18n

class SyncView extends Dialog {
  title = I18n("sync.title")
  modal = true
  preferredSize = new Dimension(450, 180)
  resizable = false
  val username = new TextField("admin")
  val password = new PasswordField("admin")
  val btUp = new Button(I18n("sync.up"))
  val btDown = new Button(I18n("sync.down"))
  val btNo = new Button(I18n("cancel"))
  val progress = new ProgressBar() {
    min = 0
    max = 100
  }
  defaultButton = btDown
  contents = new MigPanel("fill") {
    add(new Label(I18n("username")))
    add(username, "wrap, sg a, w 150")
    add(new Label(I18n("password")))
    add(password, "wrap, sg a")
    add(btUp)
    add(btDown)
    add(btNo, "wrap")
    add(progress, "span 3, grow")
  }
}

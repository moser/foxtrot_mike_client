package fmclient.views

import swing._
import fmclient.models.I18n

class SyncView extends MigPanel("fill", "[180][180][grow]") {
  val username = new TextField()
  val password = new PasswordField()
  val btUp = new Button(I18n("sync.up"))
  val btDown = new Button(I18n("sync.down"))
  val progress = new ProgressBar() {
    min = 0
    max = 100
  }
  val info = new TextArea() {
    enabled = false
  }
  add(new Label(I18n("username")))
  add(username, "w 150, sg a")
  add(new ScrollPane(info), "spany 3, grow, wrap")
  add(new Label(I18n("password")))
  add(password, "wrap, sg a")
  add(btUp)
  add(btDown, "wrap")
  add(progress, "span, grow, h 25")
  
  private var _enabled = true
  override def enabled = _enabled
  override def enabled_=(b:Boolean) = {
    _enabled = b
    username.enabled = b
    password.enabled = b
    btUp.enabled = b
    btDown.enabled = b
  }
}

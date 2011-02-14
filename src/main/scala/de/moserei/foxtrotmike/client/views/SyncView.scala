package de.moserei.foxtrotmike.client.views

import swing._
import de.moserei.foxtrotmike.client.models.I18n

class SyncView extends Dialog {
  title = I18n("sync.title")
  modal = true
  preferredSize = new Dimension(450, 180)
  resizable = false
  val username = new TextField()
  val password = new PasswordField()
  val btUp = new Button(I18n("sync.up"))
  val btDown = new Button(I18n("sync.down"))
  val btNo = new Button(I18n("close"))
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
  
  private var _enabled = true
  def enabled = _enabled
  def enabled_=(b:Boolean) = {
    _enabled = b
    username.enabled = b
    password.enabled = b
    btUp.enabled = b
    btDown.enabled = b
    btNo.enabled = b
    
    if(!b) {
      peer.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE)
    } else {
      peer.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE)
    }
  }
}

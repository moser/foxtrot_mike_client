package fmclient.views

import swing._
import java.text.SimpleDateFormat
import fmclient.models.repos._
import fmclient.models._
import fmclient.views.AutoCompleter._

class SyncView extends MigPanel("fill", "[180][180][grow]") {
  val date = new FormattedTextField(new SimpleDateFormat("dd.MM.yyyy")) {
    focusLostBehavior = FormattedTextField.FocusLostBehavior.CommitOrRevert
  }
  val airfieldRenderer = new AirfieldRenderer
  val airfield = new AutoCompleter(new EnabledFirstAutoCompleterModel[Airfield](AllAirfields, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)
  val btPrint = new Button(I18n("sync.print"))

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
  add(btDown)
  add(btUp, "wrap")
  add(progress, "span, grow, h 25")
  add(date, "w 70::, sg foo")
  peer.add(airfield, "sg foo")
  add(btPrint, "wrap")
  
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

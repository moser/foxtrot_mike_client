package fmclient.views

import swing._
import java.text.DateFormat
import java.awt.Insets
import javax.swing.{JFormattedTextField, Icon, BorderFactory}
import fmclient.models.repos._
import fmclient.models._
import fmclient.views.AutoCompleter._
import java.awt.Color
import com.wordpress.tips4java.ComponentBorder


class MyFormattedTextField(f: javax.swing.JFormattedTextField.AbstractFormatterFactory) extends FormattedTextField(null) {
  override lazy val peer: JFormattedTextField = new JFormattedTextField(f) with SuperMixin {
    override def processFocusEvent(e:java.awt.event.FocusEvent) {
      super.processFocusEvent(e)
      selectAll
    }
  }
}

class FlightView extends MigPanel("", "[70!]5[80!]5[120!]5[120!]5[80!]3[80!]5[70!]3[70!]5[60!]5[120!]") {
  val departureDate = new FormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT)) {
    focusLostBehavior = FormattedTextField.FocusLostBehavior.CommitOrRevert
  }
  val planeRenderer = new DefaultItemRenderer[Plane] {
    override def renderForList(o : AutoCompleter.Option[Plane]) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Plane]]) {
        "<html>" + o.get.registration + "<br/><font color=gray><i>" + o.get.make + "</i></font></html>"
      } else {
        o.toStringForList
      }
    }
  }
  val plane = new AutoCompleter(new DefaultAutoCompleterModel[Plane](AllPlanes, _.registration, Map("allowNil" -> false)), planeRenderer)
  val seat1 = new AutoCompleter(new Seat1ACModel)
  val seat2 = new AutoCompleter(new Seat2ACModel)
  val airfieldRenderer = new DefaultItemRenderer[Airfield] {
    override def renderForList(o : AutoCompleter.Option[Airfield]) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Airfield]]) {
        if(o.get.registration == null || o.get.registration.equals(""))
          o.get.name
        else if(o.get.name == null || o.get.name.equals(""))
          o.get.registration
        else
          "<html>" + o.get.name + "<br/><font color=gray><i>" + o.get.registration + "</i></font></html>"
      } else {
        o.toStringForList
      }
    }
  }
  val from = new AutoCompleter(new DefaultAutoCompleterModel[Airfield](AllAirfields, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)
  val to = new AutoCompleter(new DefaultAutoCompleterModel[Airfield](AllAirfields, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)

  val btDepartureTime = new InnerButton()
  val departureTime = new MyFormattedTextField(TimeFormatterFactory)
  new ComponentBorder(btDepartureTime.peer).install(departureTime.peer)
  val btArrivalTime = new InnerButton()
  val arrivalTime = new MyFormattedTextField(TimeFormatterFactory)
  new ComponentBorder(btArrivalTime.peer).install(arrivalTime.peer)
  val duration = new TextField {
    enabled = false
  }
  val engineDuration = new MyFormattedTextField(TimeFormatterFactory)
  val controller = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false)))
  val launchType = new ComboBox(List[LaunchItem](LaunchItem("SelfLaunch"),
                                                  LaunchItem("WireLaunch"),
                                                  LaunchItem("TowLaunch")))
  protected var _launchPanel : MigPanel = _
  protected var launchPanelWrapper = new MigPanel("ins 0, fill")
  launchPanelWrapper.border = BorderFactory.createLineBorder(Color.black)
  def launchPanel = _launchPanel
  def launchPanel_=(p : MigPanel) = { 
    if(_launchPanel != null) {
      launchPanelWrapper.remove(_launchPanel)
    }
    _launchPanel = p
    launchPanelWrapper.add(_launchPanel, "grow")
    revalidate
  }
  launchPanel = new MigPanel("ins 0")
  val controlPanel = new MigPanel("ins 0")
  val btSave = new Button(I18n("save"))
  val btDelete = new Button(I18n("delete"))

  val costHint = new MyComboBox[CostHint](new MyComboBoxModel(AllCostHints, true))
  val comment = new TextField

  add(new Label(I18n("departure_date")))
  add(new Label(I18n("plane")))
  add(new Label(I18n("seat1")))
  add(new Label(I18n("seat2")))
  add(new Label(I18n("from")))
  add(new Label(I18n("to")))
  add(new Label(I18n("departure_time")))
  add(new Label(I18n("arrival_time")))
  add(new Label(I18n("duration")))
  add(new Label(I18n("controller")), "wrap")

  add(departureDate, "w 70!")
  peer.add(plane, "w 80!")
  peer.add(seat1, "w 120!, sg names")
  peer.add(seat2, "sg names")
  peer.add(from, "w 80!, sg airfields")
  peer.add(to, "sg airfields")
  add(departureTime, "w 70!, sg times")
  add(arrivalTime, "sg times")
  add(duration, "w 60!, sg durations")
  peer.add(controller, "sg names, wrap")

  add(launchType)
  add(launchPanelWrapper, "gap 0, grow, spanx 9, wrap")
  add(controlPanel, "spanx 10")
  controlPanel.add(btSave)
  controlPanel.add(btDelete)
  controlPanel.add(new Label(I18n("engine_duration")))
  controlPanel.add(engineDuration, "w 60!")
  controlPanel.add(new Label(I18n("cost_hint")))
  controlPanel.add(costHint)
  controlPanel.add(new Label(I18n("comment")))
  controlPanel.add(comment)

  var _enabled = true
  override def enabled = _enabled
  override def enabled_=(b:Boolean) = {
    _enabled = b
    departureDate.enabled = b
    btDepartureTime.enabled = b
    departureTime.enabled = b
    btArrivalTime.enabled = b
    arrivalTime.enabled = b
    launchType.enabled = b
    btSave.enabled = b
    btDelete.enabled = b
    costHint.enabled = b
    comment.enabled = b

    plane.setEnabled(b)
    seat1.setEnabled(b)
    seat2.setEnabled(b)
    controller.setEnabled(b)
    from.setEnabled(b)
    to.setEnabled(b)

    _launchPanel.enabled = b
  }
}

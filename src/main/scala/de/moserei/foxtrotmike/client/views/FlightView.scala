package de.moserei.foxtrotmike.client.views

import swing._
import java.text.DateFormat
import javax.swing.JFormattedTextField
import de.moserei.foxtrotmike.client.models.repos._
import de.moserei.foxtrotmike.client.models._
import de.moserei.foxtrotmike.client.views.AutoCompleter.DefaultItemRenderer
import java.awt.Color


class MyFormattedTextField(f: javax.swing.JFormattedTextField.AbstractFormatterFactory) extends FormattedTextField(null) {
  override lazy val peer: JFormattedTextField = new JFormattedTextField(f) with SuperMixin
}

class FlightView extends MigPanel("") {
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
  val seat1 = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false)))
  val seat2 = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name))
  val airfieldRenderer = new DefaultItemRenderer[Airfield] {
    override def renderForList(o : AutoCompleter.Option[Airfield]) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Airfield]]) {
        if(o.get.registration == null || o.get.registration.equals(""))
          o.get.name
        else if(o.get.name == null || o.get.name.equals(""))
          o.get.registration
        else
          "<html>" + o.get.registration + "<br/><font color=gray><i>" + o.get.name + "</i></font></html>"
      } else {
        o.toStringForList
      }
    }
	}
  val from = new AutoCompleter(new DefaultAutoCompleterModel[Airfield](AllAirfields, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)
  val to = new AutoCompleter(new DefaultAutoCompleterModel[Airfield](AllAirfields, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)
  val departureTime = new MyFormattedTextField(TimeFormatterFactory)
  val arrivalTime = new MyFormattedTextField(TimeFormatterFactory)
  val duration = new TextField {
    enabled = false
  }
  val controller = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false)))
  val launchType = new ComboBox(List[LaunchItem](LaunchItem("SelfLaunch"), 
                                                  LaunchItem("WireLaunch"),
                                                  LaunchItem("TowLaunch")))
  protected var _launchPanel : MigPanel = _
  protected var launchPanelWrapper = new MigPanel("ins 0, fill")
  def launchPanel = _launchPanel
  def launchPanel_=(p : MigPanel) = { 
    if(_launchPanel != null) {
      launchPanelWrapper.remove(_launchPanel)
    }
    _launchPanel = p
    launchPanelWrapper.add(_launchPanel, "grow")
    revalidate
  }
  launchPanel = new MigPanel("ins 0, fill") { background = Color.blue }
  val btNew = new Button("New")
  val btSave = new Button("Save")
  
  add(new Label(I18n("departure_date")))
  add(new Label(I18n("plane")))
  add(new Label(I18n("seat1")))
  add(new Label(I18n("seat2")))
  add(new Label(I18n("departure_time")))
  add(new Label(I18n("arrival_time")))
  add(new Label(I18n("duration")))
  add(new Label(I18n("controller")), "wrap")

  add(departureDate, "w 70")
  peer.add(plane, "w 80")
  peer.add(seat1, "w 120, sg names")
  peer.add(seat2, "sg names")
  add(departureTime, "w 70, sg times")
  add(arrivalTime, "sg times")
  add(duration, "w 60")
  peer.add(controller, "sg names, wrap")
  peer.add(from, "w 80, sg airfields, skip 4")
  peer.add(to, "sg airfields, wrap")
  add(launchType)
  add(launchPanelWrapper, "gap 0, grow, spanx 6, wrap")
  add(btNew)
  add(btSave)
  
  var _enabled = true
  override def enabled = _enabled
  override def enabled_=(b:Boolean) = {
    _enabled = b
    departureDate.enabled = b
    departureTime.enabled = b
    arrivalTime.enabled = b
    duration.enabled = b
    launchType.enabled = b
    
    plane.setEnabled(b)
    seat1.setEnabled(b)
    seat2.setEnabled(b)
    controller.setEnabled(b)
    from.setEnabled(b)
    to.setEnabled(b)
  }
}

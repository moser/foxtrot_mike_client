package de.moserei.foxtrotmike.client.views

import swing._
import java.text.DateFormat
import javax.swing.JFormattedTextField
import de.moserei.foxtrotmike.client.models.repos._
import de.moserei.foxtrotmike.client.models._
import de.moserei.foxtrotmike.client.views.AutoCompleter.DefaultItemRenderer


class MyFormattedTextField(f: javax.swing.JFormattedTextField.AbstractFormatterFactory) extends FormattedTextField(null) {
  override lazy val peer: JFormattedTextField = new JFormattedTextField(f) with SuperMixin
}

class FlightView extends MigPanel {
  val departureDate = new FormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT)) {
    focusLostBehavior = FormattedTextField.FocusLostBehavior.CommitOrRevert
  }
  val planeRenderer = new DefaultItemRenderer[Plane] {
    override def renderForList(o : AutoCompleter.Option[Plane]) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Plane]]) {
        "<html>" + o.get.registration + "<br/>" + o.get.id + "</html>"
      } else {
        o.toStringForTextfield
      }
    }
	}
  val plane = new AutoCompleter(new DefaultAutoCompleterModel[Plane](AllPlanes.all, _.registration, Map("allowNil" -> false)), planeRenderer)
  val seat1 = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople.all, _.name, Map("allowNil" -> false)))
  val seat2 = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople.all, _.name))
  val airfieldRenderer = new DefaultItemRenderer[Airfield] {
    override def renderForTextfield(o : AutoCompleter.Option[Airfield]) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Airfield]]) {
        if(o.get.registration == null || o.get.registration.equals("")) o.get.name else o.get.registration
      } else {
        o.toStringForTextfield
      }
    }
	}
  val from = new AutoCompleter(new DefaultAutoCompleterModel[Airfield](AllAirfields.all, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)
  val to = new AutoCompleter(new DefaultAutoCompleterModel[Airfield](AllAirfields.all, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)
  val departureTime = new MyFormattedTextField(TimeFormatterFactory)
  val arrivalTime = new MyFormattedTextField(TimeFormatterFactory)
  val duration = new TextField {
    enabled = false
  }
  val btNew = new Button("New")
  val btSave = new Button("Save")
  add(new Label(I18n("flight.departure_date")), "wrap")
  add(departureDate, "w 70")
  peer.add(plane, "w 100")
  peer.add(seat1, "w 120, sg names")
  peer.add(seat2, "sg names")
  add(departureTime, "w 70, sg times")
  add(arrivalTime, "sg times")
  add(duration, "w 60")
  peer.add(from, "w 80, sg airfields")
  peer.add(to, "sg airfields, wrap")
  add(btNew)
  add(btSave)
}

package fmclient.views

import swing._
import java.text.DateFormat
import javax.swing.JFormattedTextField
import fmclient.models.repos._
import fmclient.models._
import fmclient.views.AutoCompleter.DefaultItemRenderer
import java.awt.Color
import com.wordpress.tips4java.ComponentBorder


class TowLaunchView extends MigPanel("ins 0", "[80!]5[120!]5[120!]5[80!]3[80!]5[70!]3[70!]5[60!]") {
  val planeRenderer = new PlaneRenderer
  val plane = new AutoCompleter(new DefaultAutoCompleterModel[Plane](TowPlanes, _.registration, Map("allowNil" -> false)), planeRenderer)
  val seat1 = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false)))

  val airfieldRenderer = new AirfieldRenderer
  val to = new AutoCompleter(new DefaultAutoCompleterModel[Airfield](AllAirfields, (o:Airfield) => o.name + " " + o.registration, Map("allowNil" -> false)), airfieldRenderer)

  val btArrivalTime = new InnerButton()
  val arrivalTime = new MyFormattedTextField(TimeFormatterFactory)
  new ComponentBorder(btArrivalTime.peer).install(arrivalTime.peer)
  val duration = new TextField {
    enabled = false
  }

  peer.add(plane, "w 80!")
  peer.add(seat1, "w 120!")
  peer.add(to, "w 80!, skip 2")
  add(arrivalTime, "skip 1, w 70!")
  add(duration, "w 60!")

  var _enabled = true
  override def enabled = _enabled
  override def enabled_=(b:Boolean) = {
    _enabled = b
    btArrivalTime.enabled = b
    arrivalTime.enabled = b

    plane.setEnabled(b)
    seat1.setEnabled(b)
    to.setEnabled(b)
  }
}

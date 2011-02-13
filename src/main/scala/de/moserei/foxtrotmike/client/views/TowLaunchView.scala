package de.moserei.foxtrotmike.client.views

import swing._
import java.text.DateFormat
import javax.swing.JFormattedTextField
import de.moserei.foxtrotmike.client.models.repos._
import de.moserei.foxtrotmike.client.models._
import de.moserei.foxtrotmike.client.views.AutoCompleter.DefaultItemRenderer
import java.awt.Color
import com.wordpress.tips4java.ComponentBorder


class TowLaunchView extends MigPanel("ins 0", "[80!]5[120!]5[120!]5[80!]3[80!]5[70!]3[70!]5[60!]") {
  val planeRenderer = new DefaultItemRenderer[Plane] {
    override def renderForList(o : AutoCompleter.Option[Plane]) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Plane]]) {
        "<html>" + o.get.registration + "<br/><font color=gray><i>" + o.get.make + "</i></font></html>"
      } else {
        o.toStringForList
      }
    }
	}
  val plane = new AutoCompleter(new DefaultAutoCompleterModel[Plane](TowPlanes, _.registration, Map("allowNil" -> false)), planeRenderer)
  val seat1 = new AutoCompleter(new DefaultAutoCompleterModel[Person](AllPeople, _.name, Map("allowNil" -> false)))

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
}

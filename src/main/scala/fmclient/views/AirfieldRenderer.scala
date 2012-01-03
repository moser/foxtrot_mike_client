package fmclient.views

import AutoCompleter._
import fmclient.models.Airfield

class AirfieldRenderer extends DefaultItemRenderer[Airfield] {
    override def renderForList(o : AutoCompleter.Option[Airfield], replace : String) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Airfield]]) {
        if(o.get.registration == null || o.get.registration.equals(""))
          html(mark(o.get.name, replace))
        else if(o.get.name == null || o.get.name.equals(""))
          html(mark(o.get.registration, replace))
        else
          html(mark(o.get.name, replace) + "<br/><font color=gray><i>" + mark(o.get.registration, replace) + "</i></font>")
      } else {
        super.renderForList(o, replace)
      }
    }
  }

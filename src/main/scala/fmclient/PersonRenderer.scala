package fmclient.views

import AutoCompleter._
import fmclient.models.Person

class PersonRenderer extends DefaultItemRenderer[Person] {
    override def renderForList(o : AutoCompleter.Option[Person], replace : String) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Person]]) {
        if(!o.get.disabled)
          html(mark(o.get.name, replace) + "<br/><i>" + o.get.group.name + "</i>")
        else
          html("<i>" + mark(o.get.name, replace) + "<br/>" + o.get.group.name + "</i>")
      } else {
        super.renderForList(o, replace)
      }
    }
}


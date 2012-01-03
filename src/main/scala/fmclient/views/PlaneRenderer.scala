package fmclient.views

import AutoCompleter._
import fmclient.models.Plane

class PlaneRenderer extends DefaultItemRenderer[Plane] {
    override def renderForList(o : AutoCompleter.Option[Plane], replace : String) = {
      if(o.isInstanceOf[AutoCompleter.RealOption[Plane]]) {
        html(mark(o.get.registration, replace) + "<br/><font color=gray><i>" + o.get.make + "</i></font>")
      } else {
        super.renderForList(o, replace)
      }
    }
}


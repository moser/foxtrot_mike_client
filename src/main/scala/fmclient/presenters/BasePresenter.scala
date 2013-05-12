package fmclient.presenters;

import java.awt.{ Color, Component => AWTComponent }
import scala.swing.Component
import fmclient.views.Colors

abstract class BasePresenter[M,V] extends AbstractPresenter {
  var mappings : List[Mapping[M,V]] = Nil
  var model:M
  var view:V

  class Mapping[M, V](vtm: (M,V) => Unit, mtv : (M,V) => Unit) {
    def updateModel(m:M, v:V) = vtm(m,v)
    def updateView(m:M, v:V) = mtv(m,v)
  }

  def updateModel = {
    if(model != null) mappings.map(m => m.updateModel(model, view))
  }

  def updateView = {
    if(model != null) mappings.map(m => m.updateView(model, view))
  }

  protected def map(vtm : (M,V) => Unit, mtv : (M,V) => Unit) {
    mappings = (new Mapping[M,V](vtm, mtv))::mappings
  }

  protected def mapViewOnly(mtv : (M,V) => Unit) {
    map((m,v) => {}, mtv)
  }

  def shutdown = {}

  protected def markIfInvalid(c : Component, valid : Boolean) : Unit = {
    markIfInvalid(c.peer, valid)
  }

  protected def markIfInvalid(c : AWTComponent, valid : Boolean) : Unit = {
    if(!valid) {
      c.setBackground(Colors.colInvalid)
    } else {
      c.setBackground(null)
    }
  }

  protected def markIfHasProblems(c : Component, hasProblems : Boolean) : Unit = {
    markIfHasProblems(c.peer, hasProblems)
  }

  protected def markIfHasProblems(c : AWTComponent, hasProblems : Boolean) : Unit = {
    if(hasProblems) {
      c.setBackground(Colors.colProblematic)
    } else {
      c.setBackground(null)
    }
  }

}

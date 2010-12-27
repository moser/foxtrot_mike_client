package de.moserei.foxtrotmike.client.presenters;

abstract class BasePresenter[M,V] {
  var mappings : List[Mapping[M,V]] = Nil
  var model:M
  var view:V

  class Mapping[M, V](mtv: (M,V) => Unit, vtm : (M,V) => Unit) {
    def updateModel(m:M, v:V) = mtv(m,v)
    def updateView(m:M, v:V) = vtm(m,v)
  }

  def updateModel {
    if(model != null) mappings.map(m => m.updateModel(model, view))
  }

  def updateView {
    if(model != null) mappings.map(m => m.updateView(model, view))
  }

  protected def map(mtv : (M,V) => Unit, vtm : (M,V) => Unit) {
    mappings = (new Mapping[M,V](mtv, vtm))::mappings
  }
}
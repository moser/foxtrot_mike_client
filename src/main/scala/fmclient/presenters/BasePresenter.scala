package fmclient.presenters;

abstract class BasePresenter[M,V] {
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
}

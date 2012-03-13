package fmclient.models

//TODO split update into inserted and removed
trait Observer {
  def created(o : BaseModel[_])
  def updated(o : BaseModel[_])
  def removed(o : BaseModel[_])
}

trait Observalbe {
  protected var observers : List[Observer] = List[Observer]()
  def addObserver(o : Observer) : Unit = {
    observers = o :: observers
  }

  protected def notifyCreated(a : BaseModel[_]) = {
    observers.foreach(_.created(a))
  }

  protected def notifyUpdated(a : BaseModel[_]) = {
    observers.foreach(_.updated(a))
  }

  protected def notifyRemoved(a : BaseModel[_]) = {
    observers.foreach(_.removed(a))
  }
}

//object NoObserver extends Observer[Any] {
//  def update(s : Any) = {}
//}

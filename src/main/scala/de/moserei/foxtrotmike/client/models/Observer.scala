package de.moserei.foxtrotmike.client.models

//TODO split update into inserted and removed
trait Observer {
  def created(o : BaseModel)
  def updated(o : BaseModel)
  def removed(o : BaseModel)
}

trait Observalbe {
  protected var observers : List[Observer] = List[Observer]()
  def addObserver(o : Observer) : Unit = {
    observers = o :: observers
  }
  
  protected def notifyCreated(a : BaseModel) = {
    observers.foreach(_.created(a))
  }
  
  protected def notifyUpdated(a : BaseModel) = {
    observers.foreach(_.updated(a))
  }
   
  protected def notifyRemoved(a : BaseModel) = {
    observers.foreach(_.removed(a))
  }
}

//object NoObserver extends Observer[Any] {
//  def update(s : Any) = {}
//}

package de.moserei.foxtrotmike.client.models

trait Observer {
  def update(o : BaseModel) //= { println("Observer#update") }
}

//object NoObserver extends Observer[Any] {
//  def update(s : Any) = {}
//}

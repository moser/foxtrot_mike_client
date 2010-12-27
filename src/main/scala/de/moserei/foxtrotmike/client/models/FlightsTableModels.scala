package de.moserei.foxtrotmike.client.models

object FlightsTableModels {
  var inst : List[FlightsTableModel] = Nil //List[FlightsTableModel]()
  def addInstance(i : FlightsTableModel) {
    inst = i :: inst
  }
  def insert(f : Flight) {
    inst.foreach(_.insert(f))
  }
  def update(f : Flight) {
    inst.foreach(_.update(f))
  }
}
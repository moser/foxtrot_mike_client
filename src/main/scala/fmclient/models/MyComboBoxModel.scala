package fmclient.models

import javax.swing.DefaultComboBoxModel
import fmclient.models.Observer
import fmclient.models.repos.BaseEntityRepository
import scalaj.collection.Imports._

//TODO should depend on BaseEntityRepository#all (reload often, see DefaultAutoCompleterModel)
class MyComboBoxModel[T <: BaseModel[_]](a : BaseEntityRepository[T, _]) extends DefaultComboBoxModel(new java.util.Vector(a.all.asJava)) with Observer {
  def created(m : BaseModel[_]) = {
    m match {
      case f : T => {
        addElement(f)
      }
      case _ => {}
    }
  }

  def updated(m : BaseModel[_]) = {}
  
  def removed(m : BaseModel[_]) {
   m match {
      case f : T => {
        removeElement(f)
      }
      case _ => {}
    }
  }
}

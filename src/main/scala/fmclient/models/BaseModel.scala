package fmclient.models

import java.util.Date
import javax.persistence._

abstract class BaseModel[T] extends Observalbe {
  def disabled : Boolean
  def disabled_=(b : Boolean) : Unit
  def id : T
  def id_=(s: T) : Unit
  def status : String
  def status_=(s: String) : Unit
  def save = {
    val persisted = isPersisted
    beforeSave(!persisted)
    val d = new Date
    if(!persisted) created_at = d
    updated_at = d
    EntityMgr.withTransaction(_.persist(this))
    afterSave(!persisted)
    if(persisted) 
      notifyUpdated(this)
    else
      notifyCreated(this)
  }
  def isPersisted = EntityMgr.em.find(this.getClass, id) != null
  def delete { delete(true) }
  def delete(notify : Boolean) {
    if(isPersisted) {
      beforeDelete
      EntityMgr.withTransaction(_.remove(this))
      afterDelete
      if(notify)
        notifyRemoved(this)
    }
  }
  
  def isValid = invalidFields.isEmpty
  def invalidFields = List[String]()

  def beforeSave(create : Boolean) = {}
  def afterSave(create : Boolean) = {}
  
  def beforeDelete = {}
  def afterDelete = {}

  @Temporal(TemporalType.TIMESTAMP)
  var created_at : Date = new Date
  @Temporal(TemporalType.TIMESTAMP)
  var updated_at : Date = new Date

  def sortingKey = toString
}


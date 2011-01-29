package de.moserei.foxtrotmike.client.models

import dispatch.json.{JsObject, JsString, JsValue, JsNull}
import dispatch.json.Js._
import java.util.Date
import javax.persistence._

abstract class BaseModel {
  var observers = List[Observer]()
  def id : String
  def id_=(s: String) : Unit
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
    afterSaveInternal
  }
  def isPersisted = EntityMgr.em.find(this.getClass, id) != null
  def delete = {
    if(isPersisted) {
      beforeDelete
      EntityMgr.withTransaction(_.remove(this))
      afterDelete
    }
  }

  def beforeSave(create : Boolean) = {}
  def afterSave(create : Boolean) = {}
  
  def beforeDelete = {}
  def afterDelete = {}
  
  def afterSaveInternal = {
    observers.foreach(o => {
      o.update(this)
    })
  }

  def this(o:JsObject) {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  final def update(o:JsObject) : Unit = {
    //assert(id.equals(('id ! str)(o))) //hmm
    pUpdate(o)
    status = "remote"
  }
  protected def pUpdate(o:JsObject) : Unit
  
  def toJson : JsObject = {
    JsObject(Map(JsString("id") -> JsString(id)) ++ jsonValues);
  }
  
  def jsonValues : Map[JsString, JsValue]

  @Temporal(TemporalType.TIMESTAMP)
  var created_at : Date = new Date
  @Temporal(TemporalType.TIMESTAMP)
  var updated_at : Date = new Date
  
  protected def stringToJson(s: String) = {
    if(s == null) 
      JsNull
    else
      JsString(s)
  }
  
  protected def idToJson(o : { def id : String }) = {
    if(o == null)
      JsNull
    else
      JsString(o.id)
  }
}


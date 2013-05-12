package fmclient.models

import dispatch.json.{JsObject, JsString, JsValue, JsNull, JsNumber}
import dispatch.json.Js._
import java.util.Date
import javax.persistence._

abstract class BaseModel[T] extends Observalbe {
  private var deleted_ = false
  def deleted = deleted_
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
  def delete = {
    if(isPersisted) {
      beforeDelete
      EntityMgr.withTransaction(_.remove(this))
      afterDelete
      deleted_ = true
      notifyRemoved(this)
    }
  }
  
  def isValid = invalidFields.isEmpty
  def invalidFields = List[String]()

  def beforeSave(create : Boolean) = {}
  def afterSave(create : Boolean) = {}
  
  def beforeDelete = {}
  def afterDelete = {}

  def this(o:JsObject) {
    this()
    update(o)
  }

  final def update(o:JsObject) : Unit = {
    //assert(id.equals(('id ! str)(o))) //hmm
    // id = ('id ! str)(o)
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
  
  protected def idToJsonInt(o : { def id : Int }) = {
    if(o == null)
      JsNull
    else
      JsNumber(o.id)
  }
}


package de.moserei.foxtrotmike.client.models

import dispatch.json.JsObject
import dispatch.json.Js._

abstract class BaseModel {
  def id : String
  def id_=(s: String) : Unit
  def save = {
    val persisted = isPersisted
    beforeSave(!persisted)
    EntityMgr.withTransaction(_.persist(this))
    afterSave(!persisted)
  }
  def isPersisted = EntityMgr.em.find(this.getClass, id) != null

  def beforeSave(create : Boolean) = {}
  def afterSave(create : Boolean) = {}

  def this(o:JsObject) {
    this()
    id = ('id ! str)(o)
    update(o)
  }

  def update(o:JsObject) : Unit
}


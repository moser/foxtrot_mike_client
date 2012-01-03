package fmclient.models

import javax.persistence._
import scalaj.collection.Imports._

object EntityMgr {
  private var emf : EntityManagerFactory = _
  private var _em : EntityManager = _

  def em = { 
    init(false)
    _em
  }

  private var initialized = false

  def init(test:Boolean) = {
    if (!initialized) {
      var properties = Map("javax.persistence.jdbc.url" -> ("jdbc:h2:" + System.getProperty("user.dir") + "/data"))
      if(test) {
        properties = Map("javax.persistence.jdbc.url" -> ("jdbc:h2:mem:test"))
      }
      emf = Persistence.createEntityManagerFactory("default", properties.asJava)
      _em = emf.createEntityManager
      initialized = true
    }
  }


  def withTransaction(f: EntityManager => Unit) = {
    init(false)
    val t = _em.getTransaction
    try {
      t.begin
      f(_em)
      t.commit
    } catch {
      case e => {
        t.rollback
        println("Error in EntityMgr#withTransaction")
        e.printStackTrace
      }
    }
  }

  def close = {
    em.close
    emf.close
    initialized = false
  }
}

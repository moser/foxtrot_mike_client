package de.moserei.foxtrotmike.client.models

import javax.persistence._
import scalaj.collection.Imports._

object EntityMgr {
  val properties = Map("javax.persistence.jdbc.url" -> ("jdbc:h2:" + System.getProperty("user.dir") + "/data"))
  val emf = Persistence.createEntityManagerFactory("default", properties.asJava)
  val em = emf.createEntityManager
  
  def withTransaction(f: EntityManager => Unit) = {
    val t = em.getTransaction
    try {
      t.begin
      f(em)
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
  }
}

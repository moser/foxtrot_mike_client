package fmclient.models

import javax.persistence._
import fmclient.models.repos.AllGroups

@Entity
@Table(name="groups")
class Group extends BaseModel[Int] with UUIDHelper {
  addObserver(AllGroups)

  @Id
  var id : Int = _

  var name = ""
  var status = "local"

  override def toString = name
}

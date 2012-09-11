package fmclient.models.repos

import fmclient.models.Attribute

object AllAttributes extends BaseStringIndexedEntityRepository[Attribute] {
  override val orderBy = "x.id ASC"

  def read(id : String) = {
    val o = find(id)
    if(o == null)
      ""
    else
      o.value
  }

  def write(id : String, value : String) = {
    var o = find(id)
    if(o == null)
      o = new Attribute(id, value)
    else
      o.value = value
    o.save
  }
}

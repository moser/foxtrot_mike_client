package fmclient.models

import fmclient.models.repos.AllAttributes

object Config {
  private var _lastUser = AllAttributes.read("lastUser")
  def lastUser = _lastUser
  def lastUser_=(value : String) = {
    _lastUser = value
    if(value != null)
      AllAttributes.write("lastUser", value)
  }

  val server = "fm.ssv-cham.de"
  val port = 80
}

package fmclient.models

import dispatch.json.{JsObject, JsString, JsNumber, JsValue}
import dispatch.json.Js
import dispatch.json.Js._

object Config {
  private val fileName = System.getProperty("user.dir") + "/fmConfig.json"
  
  private var _lastUser = ""
  private var _server = ""
  private var _port = 80
  
  if(new java.io.File(fileName).exists) {
    val json = Js(scala.io.Source.fromFile(fileName).mkString)
    _lastUser = ('lastUser ! str)(json)
    _server = ('server ! str)(json)
    _port = ('port ! num)(json).toInt
  }
  
  def lastUser = _lastUser
  def lastUser_=(l:String) = {
    _lastUser = l
    save
  }
  
  def server = _server
  def server_=(s:String) = {
    _server = s
    save
  }
  
  def port = _port
  def port_=(p:Int) = {
    _port = p
    save
  }

  def toJson = {
    JsObject(Map(JsString("lastUser") -> JsString(lastUser),
                 JsString("server") -> JsString(server),
                 JsString("port") -> JsNumber(port)))
  }
  
  def save = {
    val w = new java.io.FileWriter(fileName)
    w.write(toJson.toString)
    w.close
  }
}

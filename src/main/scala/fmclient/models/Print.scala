package fmclient.models

import org.joda.time.DateTime
import java.util.Date
import java.io._
import scalaj.http._
import scala.sys.process._

object Print  {
  case class HttpCode(code: Integer) extends Exception

  def getPdf(username: String, password: String, airfield: Airfield, date: Date) = {
    val url = "http://"+ Config.server + ":" + Config.port + "/airfields/" + airfield.id + "/main_log_book?date=" + new DateTime(date).toString("yyyy-MM-dd")
    val (responseCode, headersMap, resultBytes) = Http(url).option(HttpOptions.connTimeout(10000)).option(HttpOptions.readTimeout(50000)).auth(username, password).asHeadersAndParse(Http.readBytes)
    println(responseCode)
    if (responseCode == 200) {
      val out = new FileOutputStream("tmp.pdf")
      out.write(resultBytes)
      out.close
      val os = System.getProperty("os.name")
      if(os.startsWith("Windows")) {
        "cmd /c start tmp.pdf".!!
      } else if(os.contains("OS X")) {
        "open tmp.pdf".!!
      } else {
        "xdg-open tmp.pdf".!!
      }
    } else {
      throw HttpCode(responseCode)
    }
  }
};

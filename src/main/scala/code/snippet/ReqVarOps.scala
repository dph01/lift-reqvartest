package code.snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import net.liftweb.common.Logger
import net.liftweb.mapper._

object ReqVarString extends Logger {
  private object nameVar extends RequestVar[String]("default")
 
  def edit = {
    "name=name" #> SHtml.text(nameVar.is.toString, x => nameVar(x)) &
    "type=submit" #> SHtml.onSubmitUnit(() => { debug("name: " + nameVar)})
  }
  
  def list = {
    var name = "fred"
    "*" #> { Text(name) ++ Text(" ") ++ SHtml.link("/reqvarstringedit", () => {nameVar(name)}, Text("edit")) }
  }
}

object ReqVarEvent extends Logger {
  private object eventVar extends RequestVar[MyEvent](null)
  
  def edit = {
    "name=name" #> SHtml.text(eventVar.is.eventName.is, x => {eventVar.is.eventName(x)}) &
    "type=submit" #> SHtml.onSubmitUnit(() => {debug("name: " + eventVar.is.eventName.is)})
  }
  
  def list = {
    var e = MyEvent.create.eventName("bob")
    "*" #> { Text(e.eventName.is) ++ Text(" ") ++ SHtml.link("/reqvareventedit", () => {eventVar(e)}, Text("edit")) }
  }
}

class MyEvent extends LongKeyedMapper[MyEvent] 
    with IdPK with Logger {
    def getSingleton = MyEvent 
    
    object eventName extends MappedString(this, 30)
}

object MyEvent extends MyEvent with CRUDify[Long,MyEvent]
  with LongKeyedMetaMapper[MyEvent] {}

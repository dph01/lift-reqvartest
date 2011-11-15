package code.snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb._
import net.liftweb.http._
import util._
import common._
import Helpers._
import TimeHelpers._
import net.liftweb.common.Logger
import net.liftweb.mapper._

object ReqVarString extends Logger {
  private object nameVar extends RequestVar[String]("default")
 
  def edit = {
    "name=name" #> SHtml.text(nameVar.is.toString, x => nameVar(x)) &
    "type=submit" #> SHtml.onSubmitUnit(() => { debug("name: " + nameVar)})
  }
  
  def list = {
     ".row *" #> List("fred","harry").map( n => {
      ".eventName *" #> Text(n) &
      ".actions *" #> {SHtml.link("/reqvarstringedit", () => {nameVar(n)}, Text("edit")) }
    } )
  }
}

object ReqVarEvent extends Logger {
  private object eventVar extends RequestVar[Event](null)
  
  def edit = {
    "name=name" #> SHtml.text(eventVar.is.eventName.is, x => {debug("setting ..."); eventVar.is.eventName(x); debug("...done")}) &
    "type=submit" #> SHtml.onSubmitUnit(() => {debug("name: " + eventVar.is.eventName.is)})
  }
  
  def list = {
    ".row *" #> Event.findAll.map( t => {
      ".eventName *" #> Text(t.eventName) &
      ".actions *" #> {SHtml.link("/reqvareventedit", () => {eventVar(t)}, Text("edit")) }
    } )
  }
}

object Dummy extends Logger {
  debug("creating new Dummy")
  //var time = TimeHelpers.millis
}

class ReqVarEventEditOnly extends Logger {
  
  object eventVarEdit extends RequestVar[Event](Event.create.eventName("default"))
  object Dummy extends Logger { debug("creating new Dummy"); var time = TimeHelpers.millis }
  //var dummy = new Dummy
  debug("dummy is: " + Dummy.time )
  
  def setName(name: String ) = {
    debug("in callback to setName with value: " + name + " ....") 
    debug("eventVar.eventName before being set: " + eventVarEdit.is.eventName)
    debug("dummy is: " + Dummy.time )
    eventVarEdit.is.eventName(name)
    debug("...done in callback to set name")
  }  
  def setNotes(name: String ) = {
    debug("in callback to setNotes with value: " + name + " ....") 
    debug("eventVar.notes before being set: " + eventVarEdit.is.notes)
    debug("dummy is: " + Dummy.time)
    eventVarEdit.is.notes(name)
    debug("...done in callback to setNotes")
  }
  
  def edit = {
    "name=name" #> SHtml.text(eventVarEdit.is.eventName.is, setName(_)) &
    "name=notes" #> SHtml.text(eventVarEdit.is.notes.is, setNotes(_)) &
    "type=submit" #> SHtml.onSubmitUnit(() => {debug("processing submit for name" )})
  }

}

class Event extends LongKeyedMapper[Event] 
    with IdPK with Logger {
    def getSingleton = Event 
    debug("creating new Event")
    object eventName extends MappedString(this, 30)
    object notes extends MappedString(this, 30)
}

object Event extends Event 
  with LongKeyedMetaMapper[Event] {}

/*
 * State preserved:
object OnSubmit {
  var name = ""
    
  def render = {
    // define some variables to put our values into
   "name=name" #> SHtml.text(name, name = _ ) &
   "type=submit" #> SHtml.onSubmitUnit(() => println("processing submit"))
  }
}
// tidied + vars moved out of render + onSubmit => text
object OnSubmit {
  var name = ""
  var age = 0
  def render = {
    "name=name" #> SHtml.text(name, name = _) & // set the name
    "type=submit" #> SHtml.onSubmitUnit(() => { println("processing submit") } )
  }
}
object OnSubmit {
  var name = ""
  var age = 0
  def render = {
    "name=name" #> SHtml.text(name, name = _) & // set the name
    "type=submit" #> SHtml.onSubmitUnit(() => { println("processing submit") } )
  }
}
////////////////////////////////////////////////////////// */
/* State not preserved:
 * 
// tidied + vars moved out of render + onSubmit => text + object -> class
class OnSubmit {
  var name = ""
  var age = 0
  def render = {
    "name=name" #> SHtml.text(name, name = _) & // set the name
    "type=submit" #> SHtml.onSubmitUnit(() => { println("processing submit") } )
  }
}

 
// tidied + onSubmit => text
object OnSubmit {
  def render = {
    var name = ""
    var age = 0
    "name=name" #> SHtml.text(name, name = _) & // set the name
    "type=submit" #> SHtml.onSubmitUnit(() => { println("processing submit") } )
  }
}
  
// tidied + var moved out of render
object OnSubmit {
  var name = ""
  var age = 0
  def render = {
    "name=name" #> SHtml.onSubmit(name = _) & // set the name
    "type=submit" #> SHtml.onSubmitUnit(() => { println("processing submit") } )
  }
}
// tidied up
 object OnSubmit {
  def render = {
    var name = ""
    var age = 0
    "name=name" #> SHtml.onSubmit(name = _) & // set the name
    "type=submit" #> SHtml.onSubmitUnit(() => { println("processing submit") } )
  }
}
 
 object OnSubmit {
  def render = {
    var name = ""
    var age = 0
    // define some variables to put our values into
    // process the form
    def process() {
      // if the age is < 13, display an error
      if (age < 13) S.error("Too young!")
      else {
        // otherwise give the user feedback and
        // redirect to the home page
        S.notice("Name: "+name)
        S.notice("Age: "+age)
        S.redirectTo("/")
      }
    }
    // associate each of the form elements
    // with a function... behavior to perform when the
    // for element is submitted
    "name=name" #> SHtml.onSubmit(name = _) & // set the name
    // set the age variable if we can convert to an Int
    "name=age" #> SHtml.onSubmit(s => asInt(s).foreach(age = _)) &
    // when the form is submitted, process the variable
    "type=submit" #> SHtml.onSubmitUnit(process)
  }
}
 
 =================================================
 */
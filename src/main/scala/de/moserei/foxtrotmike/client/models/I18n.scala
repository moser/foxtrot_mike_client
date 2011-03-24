package de.moserei.foxtrotmike.client.models

object I18n {
  def t(s:String, interpolation : Array[{ def toString : String }]) : String = {
    var res = s match {
      case "main.title" => "FoxtrotMike Flugeingabe"
      case "flight" => "Flug"
      case "departure_date" => "Datum"
      case "plane" => "Flugzeug"
      case "seat1" => "Pilot/Schüler"
      case "seat2" => "Begleiter/Lehrer"
      case "departure_time" => "Abflug"
      case "arrival_time" => "Ankunft"
      case "from" => "Von"
      case "to" => "Nach"
      case "duration" => "Dauer"
      case "controller" => "Flugleiter"
      case "operator" => "Windenfahrer"
      case "wire_launcher" => "Winde"
      case "tow_plane" => "Schleppflugzeug"
      case "airfield" => "Flugplatz"
      case "firstname" => "Vorname"
      case "lastname" => "Nachname"
      case "group" => "Gruppe"
      case "person_balloon.title" => "<html><h2>Unbekannte Person</h2>Bitte füllen Sie die folgenden Felder aus, um sie anzulegen.</html>"
      
      case "defaults" => "Standardwerte"
      
      case "WireLaunch.short" => "W"
      case "TowLaunch.short" => "F"
      case "SelfLaunch.short" => ""
      
      case "sync.title" => "Synchronisation"
      case "username" => "Benutzername"
      case "password" => "Passwort"
      case "sync.down" => "Daten herunterladen"
      case "sync.up" => "Flüge hochladen"
      case "close" => "Schließen"
      
      case "ok" => "Ok"
      case "cancel" => "Abbrechen"
      
      case "colored" => "farbig"
      case "flying-only" => "Nur Unvollendete"
      case "with-problems-only" => "Nur mit Problemen"
      
      case "new" => "Neu"
      case "save" => "Speichern"
      case "delete" => "Löschen"
      case "copy" => "Kopieren"
      case "create" => "%% anlegen"
      case _ => s
    }
    for(i <- interpolation) {
      res = res.replaceFirst("%%", i.toString)
    } 
    res
  }
  
  def t(s : String) : String = t(s, Array[{ def toString : String }]())

  def apply(s:String) = t(s)
  def apply(s:String, arr : Array[{ def toString : String }]) = t(s, arr)
}

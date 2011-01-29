package de.moserei.foxtrotmike.client.models

object I18n {
  def t(s:String) : String = {
    s match {
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
      
      case "colored" => "farbig"
      case "flying-only" => "Noch in der Luft"
      case "with-problems-only" => "Mit Problemen"
      
      case "new" => "Neu"
      case "save" => "Speichern"
      case "delete" => "Löschen"
      case "copy" => "Kopieren"
      case _ => s
    }
  }

  def apply(s:String) = t(s)
}

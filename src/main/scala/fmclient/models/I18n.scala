package fmclient.models

object I18n {
  def t(s:String, interpolation : Array[{ def toString : String }]) : String = {
    var res = s match {
      case "main.title" => "FoxtrotMike Flugeingabe"
      case "main.actions_menu.title" => "Aktionen"
      case "main.sync" => "Synchronisation"
      case "flight" => "Flug"
      case "departure_date" => "Datum"
      case "plane" => "Flugzeug"
      case "seat1" => "Pilot/Schüler"
      case "unknown_person" => "Pilot"
      case "seat2" => "Begleiter/Lehrer"
      case "departure_time" => "Abflug"
      case "arrival_time" => "Ankunft"
      case "from" => "Von"
      case "to" => "Nach"
      case "duration" => "Dauer"
      case "engine_duration" => "Motor"
      case "controller" => "Flugleiter"
      case "comment" => "Kommentar"
      case "cost_hint" => "Kostengruppe"
      case "operator" => "Windenfahrer"
      case "wire_launcher" => "Winde"
      case "tow_plane" => "Schleppflugzeug"
      case "tow_pilot" => "Schlepppilot"
      case "airfield" => "Flugplatz"
      case "liabilities" => "Bezahler"
      case "firstname" => "Vorname"
      case "lastname" => "Nachname"
      case "group" => "Gruppe"
      case "person_balloon.title" => "<html><h2>Unbekannte Person</h2>Bitte füllen Sie die folgenden Felder aus, um sie anzulegen.</html>"
      case "plane_balloon.title" => "<html><h2>Unbekanntes Flugzeug</h2>Bitte füllen Sie die folgenden Felder aus, um es anzulegen.</html>"
      case "airfield_balloon.title" => "<html><h2>Unbekannter Flugplatz</h2>Bitte füllen Sie die folgenden Felder aus, um ihn anzulegen.</html>"
      case "registration" => "Kennzeichen"
      case "make" => "Typ"
      case "default_launch_method" => "Bevorzugte Startart"
      case "legal_plane_class" => "Flugzeugklasse"
      case "has_engine" => "Motorisiert"
      case "can_fly_without_engine" => "Kann motorlos fliegen"
      case "selflaunching" => "Eigenstartfähig"
      case "can_tow" => "Kann schleppen"
      case "can_be_towed" => "Kann im F-Schlepp starten"
      case "can_be_wire_launched" => "Kann an der Winde starten"
      case "default_engine_duration_to_duration" => "Motorlaufzeit normalerweise gleich Flugzeit (z.B. Reisemotorsegler)"

      case "defaults" => "Standardwerte"

      case "wire_launch.short" => "W"
      case "tow_launch.short" => "F"
      case "self_launch.short" => "E"

      case "sync" => "Synchronisation"
      case "username" => "Benutzername"
      case "password" => "Passwort"
      case "sync.down" => "Daten herunterladen"
      case "sync.up" => "Flüge hochladen"

      case "error" => "Fehler"
      case "error.connection" => "Verbindung fehlgeschlagen. Bitte prüfen Sie die Netzwerkverbindung. Dieser Fehler kann auch am Server liegen."
      case "error.access_denied" => "Zugriff verweigert! Bitte prüfen Sie die Zugangsdaten."
      case "error.unknown_error" => "Unbekannter Fehler: "
      case "error.unprocessable" => "Der Server hat folgende Änderung abgelehnt:"
      
      case "sync.create" => "Neu"
      case "sync.update" => "Aktualisiert"
      case "sync.upload" => "Hochgeladen"

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
      res = res.replaceFirst("%%", i.toString.replace("\\", ""))
    }
    res
  }

  def t(s : String) : String = t(s, Array[{ def toString : String }]())

  def apply(s:String) = t(s)
  def apply(s:String, arr : Array[{ def toString : String }]) = t(s, arr)
}

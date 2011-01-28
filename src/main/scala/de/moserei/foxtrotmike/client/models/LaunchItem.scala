package de.moserei.foxtrotmike.client.models

case class LaunchItem(back: String) {
  override def toString() = I18n(back + ".short")
}

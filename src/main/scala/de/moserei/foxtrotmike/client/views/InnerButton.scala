package de.moserei.foxtrotmike.client.views

import swing._

class InnerButton extends Button("   ") {
  focusable = false
  margin = new Insets(0,0,0,0)
  focusPainted = false
  borderPainted = false
  border = null
}

package fmclient.presenters

import javax.swing.SwingUtilities

class AbstractPresenter {
  def doLater(a: () => Unit) {
    SwingUtilities.invokeLater(new Runnable() {
      def run() {
        a()
      }
    })
  }
}

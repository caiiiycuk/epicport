package com.epicport

import com.epicport.fs.FileModificationWatcher

import xitrum.Server
import xitrum.i18n.PoLoader

object Boot {
  def main(args: Array[String]) {
    new FileModificationWatcher("config/i18n", _ => PoLoader.clear, Some("\\.po$".r))
    Server.start()
  }

}
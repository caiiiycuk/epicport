package com.epicport.fs

import java.nio.file.FileSystems
import java.nio.file.Path
import java.io.File
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey
import scala.collection.JavaConversions._
import StandardWatchEventKinds._
import xitrum.i18n.PoLoader
import scala.util.matching.Regex

private class InvalidKeyException extends Exception

class FileModificationWatcher(dir: String, whenModified: (File) => Unit, fileMask: Option[Regex] = None) extends Thread {

  setName(s"FileModificationWatcher - [$dir]")
  setDaemon(true)
  
  private val watcher = FileSystems.getDefault().newWatchService()

  private val watchPath = new File(dir).toPath()

  watchPath.register(watcher, ENTRY_MODIFY)

  start()

  private def onFileModified(file: File) = fileMask match {
    case Some(mask) => if (mask.findFirstIn(file.toString).isDefined) {
      whenModified(file)
    }
    case None => whenModified(file)
  }

  override def run(): Unit = {
    while (true) {
      try {
        val key = watcher.take()
        val events = key.pollEvents()
        for (
          event <- events;
          kind = event.kind() if kind != OVERFLOW
        ) {
          if (event.context().isInstanceOf[Path]) {
            val path = event.context().asInstanceOf[Path]
            onFileModified(path.toFile)
          }

          if (!key.reset()) {
            throw new InvalidKeyException()
          }
        }
      } catch {
        case e: InvalidKeyException => {
          watcher.close
          return
        }
        case e: InterruptedException => {
          watcher.close
          return
        }
      }
    }
  }

}
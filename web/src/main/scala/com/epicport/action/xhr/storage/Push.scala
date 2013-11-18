package com.epicport.action.xhr.storage

import xitrum.Action
import xitrum.annotation.POST
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.File
import java.util.zip.GZIPOutputStream

@POST("/xhr/storage/push")
class Push extends Storage {

  def execute() {
    val content = request.getContent()
    val target  = profile.mapAbsoluteFile(game, fileName)

    if (!target.getParentFile().exists && !target.getParentFile().mkdirs) {
      throw new IllegalStateException(s"Unable to create directory for file ${target}")
    }

    val stream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(target)))
    content.readBytes(stream, content.readableBytes())
    stream.close
    
    respondText("ok")
  }
  
}
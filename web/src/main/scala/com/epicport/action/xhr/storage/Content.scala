package com.epicport.action.xhr.storage

import xitrum.annotation.GET
import com.epicport.action.NotFoundError

@GET("/xhr/storage/content/:game")
class Content extends Storage {

  def execute {
    val target = profile.mapAbsoluteFile(game, fileName)
    
    if (target.exists) {
      response.headers().add("Content-Encoding", "gzip")
      respondFile(target.getAbsoluteFile().toString)
    } else {
      forwardTo[NotFoundError]()
    }
  }
  
}
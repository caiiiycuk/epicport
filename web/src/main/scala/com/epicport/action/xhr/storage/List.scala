package com.epicport.action.xhr.storage

import xitrum.Action
import xitrum.annotation.GET
import java.io.File
import com.epicport.player.Profile

@GET("/xhr/storage/list/:game")
class List extends Storage {

  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._
  
  def execute {
    val json = 
      ("root" -> profile.root(game).getAbsoluteFile().toString) ~
      ("files" -> profile.list(game).toSeq)
    
    respondText(compact(render(json)), "application/json")
  }
  
}
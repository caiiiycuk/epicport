package com.epicport.action.xhr.save

import xitrum.Action
import xitrum.annotation.GET
import java.io.File

@GET("/xhr/save/list/:game/:id")
class List extends SavesXhr {

  def execute {
    val player 	= param("id")
    val game 	= param("game")
    
    val legacy = lefacyFolder(game, player)
    
    val legacySaves = if (legacy.exists) {
      legacy.list()
    } else {
      Array()
    }
    
    respondJson(legacySaves)
  }
  
}
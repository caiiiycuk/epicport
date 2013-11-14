package com.epicport.action.xhr.save

import xitrum.Action
import xitrum.annotation.POST

@POST("/xhr/save/push/:game/:id")
class Push extends SavesXhr {

  def execute() {
  }
  
}
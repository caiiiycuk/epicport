package com.epicport

import java.io.File

import xitrum.Config.application

object Configuration {

  import xitrum.Config.application
  
  val data = new File(application.getString("epicport.data"))
  
  val profile = new File(data, "profile")
  
  if (!data.exists) {
    throw new IllegalStateException(s"Folder {$data} does not exists")
  }
  
  if (!profile.exists) {
    throw new IllegalStateException(s"Folder {profile} does not exists")
  }
  
}
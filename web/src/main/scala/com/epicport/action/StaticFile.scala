package com.epicport.action

import xitrum.Action

abstract class StaticFile(fileName: String) extends Action {

  beforeFilter {
    redirectTo(publicUrl(fileName))
    false
  }

  def execute = {
    throw new UnsupportedOperationException
  }
  
}
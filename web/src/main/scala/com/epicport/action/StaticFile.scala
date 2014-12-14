package com.epicport.action

import xitrum.FutureAction

abstract class StaticFile(fileName: String) extends FutureAction {

  beforeFilter {
    redirectTo(publicUrl(fileName))
    false
  }

  def execute = {
    throw new UnsupportedOperationException
  }
  
}
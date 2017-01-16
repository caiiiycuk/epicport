package com.epicport.action

import xitrum.FutureAction
import xitrum.SkipCsrfCheck

abstract class StaticFile(fileName: String) extends FutureAction with SkipCsrfCheck  {

  beforeFilter {
    redirectTo(publicUrl(fileName))
    false
  }

  def execute = {
    throw new UnsupportedOperationException
  }
  
}
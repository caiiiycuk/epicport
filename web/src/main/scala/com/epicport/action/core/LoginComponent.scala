package com.epicport.action.core

import xitrum.FutureAction
import xitrum.SkipCsrfCheck

class LoginComponent extends FutureAction with SkipCsrfCheck  {

  def execute = {}
  
  def render = {
    renderFragment("login")
  }
  
}
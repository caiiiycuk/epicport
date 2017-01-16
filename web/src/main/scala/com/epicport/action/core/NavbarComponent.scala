package com.epicport.action.core

import xitrum.FutureAction
import xitrum.SkipCsrfCheck

class NavbarComponent extends FutureAction with SkipCsrfCheck {

  def execute = {}

  def render = {
    renderFragment("navbar")
  }
  
}
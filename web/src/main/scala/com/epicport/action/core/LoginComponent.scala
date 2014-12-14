package com.epicport.action.core

import xitrum.FutureAction

class LoginComponent extends FutureAction {

  def execute = {}
  
  def render = {
    renderFragment("login")
  }
  
}
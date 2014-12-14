package com.epicport.action.core

import xitrum.FutureAction

class CoreComponent extends FutureAction {
  
  def execute = {}

  def render = {
    renderFragment("core")
  }
  
}
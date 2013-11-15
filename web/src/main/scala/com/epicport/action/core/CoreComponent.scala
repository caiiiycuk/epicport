package com.epicport.action.core

import xitrum.Action

class CoreComponent extends Action {
  
  def execute = {}

  def render = {
    renderFragment("core")
  }
  
}
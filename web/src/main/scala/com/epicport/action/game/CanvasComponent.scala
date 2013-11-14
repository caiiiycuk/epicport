package com.epicport.action.game

import xitrum.Action

class CanvasComponent extends Action {

  def execute {
  }
  
  def render(game: String) = {
    at("game") = game
    renderFragment("canvas")
  }
  
}
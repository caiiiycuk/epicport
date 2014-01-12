package com.epicport.action.game

import com.epicport.action.DefaultLayout

case class ScreenShot(small: String, big: String)

abstract class GameDescription extends DefaultLayout {

  def screenshots: Seq[ScreenShot]
  
  def gameName: String
  
  def gameDescription: String
  
  final def execute() {
    respondView[GameDescription]()
  }

}
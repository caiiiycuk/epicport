package com.epicport.action.game

import com.epicport.action.DefaultLayout
import com.epicport.action.core.Link

case class ScreenShot(small: String, big: String)

abstract class GameDescription extends DefaultLayout {

  def gameName: String
  
  def gameDescription: String
  
  def linkToPlay: String
  
  def links: Seq[Link]
  
  def screenshots: Seq[ScreenShot]
  
  final def execute() {
    respondView[GameDescription]()
  }

}
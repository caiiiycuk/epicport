package com.epicport.action.game

import com.epicport.action.DefaultLayout
import com.epicport.action.core.Link
import xitrum.SkipCsrfCheck

case class ScreenShot(small: String, big: String)

abstract class GameDescription extends DefaultLayout with SkipCsrfCheck {

  def gameName: String
  
  def gameDescription: String
  
  def linkToPlay: String
  
  def links: Seq[Link]
  
  def screenshots: Seq[ScreenShot]
  
  final def execute() {
    respondView[GameDescription]()
  }

}
package com.epicport.action.game

import com.epicport.action.DefaultAction
import com.epicport.action.DefaultLayout
import com.epicport.action.RenderHelper
import com.epicport.action.core.Link

trait GameDescriptionV2 extends DefaultAction with RenderHelper {

  def gameName: String

  def gameDescription: String

  def linkToPlay: Link

  def links: Seq[Link]

  def screenshots: Seq[ScreenShot]

  def game: Game
  
  def games = Game.all.filter(_ != game)
  
  def downloadSizeInMb: Int
  
  def mainImageUrl: String

  final def execute() {
    respondView[GameDescriptionV2]()
  }

}
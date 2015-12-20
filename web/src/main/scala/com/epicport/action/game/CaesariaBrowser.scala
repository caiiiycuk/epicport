package com.epicport.action.game

import com.epicport.action.DefaultAction
import xitrum.annotation.GET
import com.epicport.action.StaticFile
import com.epicport.action.core.Link

@GET(":lang/caesar3/browser")
class CaesariaBrowser extends com.epicport.action.Redirect301[CaesariaDescription]

@GET("/:lang/caesar3")
class CaesariaDescription extends GameDescriptionV2 {
  def game = Game.CAESAR3
  def gameContainerFragment = Some("caesaria_browser")
  def downloadSizeInMb = 38

  def title = t("caesaria-description-title")
  def description = t("caesaria-description-description")
  def keywords = t("caesaria-description-keywords")
  def gameName = t("caesaria-description-game-name")
  def gameDescription = t("caesaria-description-game-description")
  def linkToPlay = Link(t("html_play_in_browser"))
  def links = Seq()

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("caesaria/00_small.jpg", "caesaria/00.jpg"),
      ScreenShot("caesaria/01_small.jpg", "caesaria/01.jpg"),
      ScreenShot("caesaria/02_small.jpg", "caesaria/02.jpg"),
      ScreenShot("caesaria/03_small.jpg", "caesaria/03.jpg"))

}

@GET("/:lang/caesar3/caesaria.data")
class CaesariaData extends StaticFile("emscripten/caesaria/caesaria.data")

@GET("/:lang/caesar3/caesaria.js.mem")
class CaesariaMem extends StaticFile("emscripten/caesaria/caesaria.js.mem")
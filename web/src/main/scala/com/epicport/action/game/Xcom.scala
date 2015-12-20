package com.epicport.action.game

import com.epicport.Configuration
import com.epicport.action.core.StaticPageLink
import xitrum.FutureAction
import xitrum.annotation.GET
import com.epicport.action.StaticFile
import com.epicport.action.core.Link

@GET("/:lang/xcom/browser")
class Xcom extends com.epicport.action.Redirect301[XcomDescription]

@GET("/:lang/xcom/description")
class XcomDescriptionRedircet extends com.epicport.action.Redirect301[XcomDescription]

@GET("/:lang/xcom")
class XcomDescription extends GameDescriptionV2 {
  def game = Game.XCOM
  def downloadSizeInMb = 7
  def gameContainerFragment = Some("xcom")

  def title = t("html_xcom_game_name")
  def description = t("html_xcom_description")
  def keywords = t("html_xcom_keywords")
  def gameName = t("html_xcom_game_name")
  def gameDescription = t("html_xcom_game_description_full")
  def linkToPlay = Link(t("html_play_in_browser"))
  def links = language match {
    case "ru" => Seq(StaticPageLink("xcom-story"))
    case _    => Seq()
  }

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("xcom/00_small.png", "xcom/00.png"),
      ScreenShot("xcom/01_small.png", "xcom/01.png"),
      ScreenShot("xcom/02_small.png", "xcom/02.png"),
      ScreenShot("xcom/03_small.jpg", "xcom/03.jpg"))

}

@GET("/:lang/xcom/story")
class XcomStory extends FutureAction {

  beforeFilter {
    redirectTo[StaticPage]("lang" -> param("lang"),
      "page" -> "xcom-story")
    false
  }

  def execute = {
    throw new UnsupportedOperationException()
  }

}

@GET("/:lang/openxcom.data")
class XcomData extends StaticFile("emscripten/xcom/openxcom.data")

@GET("/:lang/openxcom.js.mem")
class XcomMem extends StaticFile("emscripten/xcom/openxcom.js.mem")
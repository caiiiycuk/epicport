package com.epicport.action.game

import xitrum.annotation.GET
import com.epicport.action.core.Link
import com.epicport.action.core.StaticPageLink

@GET("/:lang/ttd/browser")
class Ttd extends GameLayout {

  def title = t("html_play_ttd_title")
  def description = t("html_play_ttd_description")
  def keywords = t("html_play_ttd_keywords")

  def execute() {
    respondView()
  }

}

@GET("/:lang/ttd/description")
class TtdDescriptionRedircet extends com.epicport.action.Redirect301[TtdDescription]

@GET("/:lang/ttd")
class TtdDescription extends GameDescriptionV2 {
  def game = Game.TTD
  def downloadSizeInMb = 7
  def mainImageUrl = publicUrl("v2/img/ttd-main-image.jpg")
  
  def linkToMultiplayer = Link(
    t("htmpl_page_description_ttd_multipalyer"), 
    url[Ttd]("lang" -> language, "multiplayer" -> "yes"), 
    "play-button ttd-multiplayer")

  def linkToPerfomanceTest = Link(
    t("htmpl_page_description_ttd_prefomance"), 
    url[Ttd]("lang" -> language, "perfomance" -> "yes"), 
    "default-link")

  def title           = t("html_page_description_ttd_name")
  def description     = t("html_page_description_ttd_description_short")
  def keywords        = t("html_page_description_ttd_keywords")
  def gameName        = t("html_page_description_ttd_name")
  def gameDescription = t("html_page_description_ttd_description")

  def linkToPlay        = Link(t("html_play_in_browser"), url[Ttd]("lang" -> language))
  def links             = language match {
    case "ru" => Seq(linkToMultiplayer, linkToPerfomanceTest, StaticPageLink("ttd-story"))
    case _ => Seq(linkToMultiplayer, linkToPerfomanceTest)
  }

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("ttd/00_small.png", "ttd/00.png"),
      ScreenShot("ttd/01_small.png", "ttd/01.png"),
      ScreenShot("ttd/02_small.png", "ttd/02.png"),
      ScreenShot("ttd/03_small.png", "ttd/03.png"))


}
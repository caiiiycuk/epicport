package com.epicport.action.game

import xitrum.annotation.GET
import com.epicport.action.core.Link
import com.epicport.action.core.StaticPageLink

@GET("/:lang/ttd/browser")
class Ttd extends com.epicport.action.Redirect301[TtdSinglePlayer]

@GET("/:lang/ttd/description")
class TtdDescriptionRedircet extends com.epicport.action.Redirect301[TtdSinglePlayer]

trait TtdDescription extends GameDescriptionV2 {
  def game = Game.TTD
  def downloadSizeInMb = 7
  def gameContainerFragment = Some("ttd")

  def title = t("html_page_description_ttd_name")
  def description = t("html_page_description_ttd_description_short")
  def keywords = t("html_page_description_ttd_keywords")
  def gameName = t("html_page_description_ttd_name")
  def gameDescription = t("html_page_description_ttd_description")

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("ttd/00_small.png", "ttd/00.png"),
      ScreenShot("ttd/01_small.png", "ttd/01.png"),
      ScreenShot("ttd/02_small.png", "ttd/02.png"),
      ScreenShot("ttd/03_small.png", "ttd/03.png"))
}

@GET("/:lang/ttd")
class TtdSinglePlayer extends TtdDescription {

  def linkToMultiplayer = Link(
    t("htmpl_page_description_ttd_multipalyer"),
    url[TtdMultiplayer]("lang" -> language),
    "play-button ttd-multiplayer")

  def linkToPerfomanceTest = Link(
    t("htmpl_page_description_ttd_prefomance"),
    url[TtdPerfomance]("lang" -> language),
    "default-link")

  def linkToPlay = Link(t("html_play_in_browser"))

  def links = language match {
    case "ru" => Seq(linkToMultiplayer, linkToPerfomanceTest, StaticPageLink("ttd-story"))
    case _    => Seq(linkToMultiplayer, linkToPerfomanceTest)
  }

}

@GET("/:lang/ttd/multiplayer")
class TtdMultiplayer extends TtdDescription {
  def linkToSingleplayer = Link(
    t("html_page_description_ttd_singlepalyer"),
    url[TtdSinglePlayer]("lang" -> language),
    "play-button ttd-multiplayer")

  def linkToPerfomanceTest = Link(
    t("htmpl_page_description_ttd_prefomance"),
    url[TtdPerfomance]("lang" -> language),
    "default-link")

  def linkToPlay = Link(t("htmpl_page_description_ttd_multipalyer"))

  def links = language match {
    case "ru" => Seq(linkToSingleplayer, linkToPerfomanceTest, StaticPageLink("ttd-story"))
    case _    => Seq(linkToSingleplayer, linkToPerfomanceTest)
  }
}

@GET("/:lang/ttd/perfomance")
class TtdPerfomance extends TtdDescription {
  def linkToSingleplayer = Link(
    t("html_page_description_ttd_singlepalyer"),
    url[TtdSinglePlayer]("lang" -> language),
    "play-button ttd-multiplayer")

  def linkToMultiplayer = Link(
    t("htmpl_page_description_ttd_multipalyer"),
    url[TtdMultiplayer]("lang" -> language),
    "play-button ttd-multiplayer")

  def linkToPlay = Link(t("htmpl_page_description_ttd_prefomance"))

  def links = language match {
    case "ru" => Seq(linkToSingleplayer, linkToMultiplayer, StaticPageLink("ttd-story"))
    case _    => Seq(linkToSingleplayer, linkToMultiplayer)
  }
}
package com.epicport.action.game

import xitrum.annotation.GET
import com.epicport.action.core.Link
import com.epicport.action.core.StaticPageLink

@GET("/:lang/dune2")
class Dune2 extends GameLayout {

  def title = t("html_play_dune2_title")
  def description = t("html_play_dune2_description")
  def keywords = t("html_play_dune2_keywords")

  def execute() {
    respondView()
  }

}

@GET("/:lang/dune2/description")
class Dune2Description extends GameDescription {

  lazy val googlePlayLink = Link(t("html_play_on_phone"), 
      "https://play.google.com/store/apps/details?id=com.gamesinjs.dune2", 
      "default-link")
  
  def title           = t("html_page_description_dune2_name")
  def description     = t("html_page_description_dune2_description_short")
  def keywords        = t("html_page_description_dune2_keywords")
  def gameName        = t("html_page_description_dune2_name")
  def gameDescription = t("html_page_description_dune2_description")
  def linkToPlay      = url[Dune2]("lang" -> getLanguage)
  def links           = getLanguage match {
    case "ru" => Seq(googlePlayLink, StaticPageLink("dune2-story"))
    case _ => Seq(googlePlayLink)
  }

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("dune2/00_small.png", "dune2/00.png"),
      ScreenShot("dune2/01_small.png", "dune2/01.png"),
      ScreenShot("dune2/02_small.png", "dune2/02.png"),
      ScreenShot("dune2/03_small.png", "dune2/03.png"))

}
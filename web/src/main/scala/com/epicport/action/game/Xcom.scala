package com.epicport.action.game

import com.epicport.Configuration
import com.epicport.action.core.StaticPageLink

import xitrum.Action
import xitrum.annotation.GET

@GET("/:lang/xcom")
class Xcom extends GameLayout {

  def title = t("html_xcom_title")
  def description = t("html_xcom_description")
  def keywords = t("html_xcom_keywords")

  def execute() {
    respondView()
  }

}

@GET("/:lang/xcom/description")
class XcomDescription extends GameDescription {

  def title           = t("html_xcom_game_name")
  def description     = t("html_xcom_description")
  def keywords        = t("html_xcom_keywords")
  def gameName        = t("html_xcom_game_name")
  def gameDescription = t("html_xcom_game_description_full")
  def linkToPlay      = url[Xcom]("lang" -> language)
  def links           = language match {
    case "ru" => Seq(StaticPageLink("xcom-story"))
    case _ => Seq()
  }
    
  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("xcom/00_small.png", "xcom/00.png"),
      ScreenShot("xcom/01_small.png", "xcom/01.png"),
      ScreenShot("xcom/02_small.png", "xcom/02.png"),
      ScreenShot("xcom/03_small.jpg", "xcom/03.jpg"))

}

@GET("/:lang/xcom/story")
class XcomStory extends Action {

  beforeFilter {
    redirectTo[StaticPage]("lang" -> param("lang"), 
        "page" -> "xcom-story")  
    false
  }
  
  def execute = {
    throw new UnsupportedOperationException()
  }

}
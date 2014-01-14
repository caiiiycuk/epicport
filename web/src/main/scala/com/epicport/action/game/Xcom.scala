package com.epicport.action.game

import xitrum.annotation.GET
import xitrum.Action
import xitrum.annotation.First
import com.epicport.Configuration

@GET("/:lang/xcom")
class Xcom extends GameLayout {

  def title = t("html_xcom_title")
  def description = t("html_xcom_description")
  def keywords = t("html_xcom_keywords")

  def execute() {
    at("demoLink") = Configuration.demoLink
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
  def linkToPlay      = url[Xcom]("lang" -> getLanguage)
  def links           = Seq()

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("xcom/00_small.png", "xcom/00.png"),
      ScreenShot("xcom/01_small.png", "xcom/01.png"),
      ScreenShot("xcom/02_small.png", "xcom/02.png"),
      ScreenShot("xcom/03_small.jpg", "xcom/03.jpg"))

}
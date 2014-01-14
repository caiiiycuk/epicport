package com.epicport.action.game

import xitrum.annotation.GET
import xitrum.Action
import xitrum.annotation.First
import com.epicport.Configuration
import com.epicport.action.DefaultLayout
import com.epicport.action.core.Link

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
  def links           = if (getLanguage == "ru") {
    Seq(Link("НЛО прилетело и опубликовало себя в браузере", 
      url[XcomStory]("lang" -> getLanguage),
      "default-link"))
  } else {
    Seq()
  }

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("xcom/00_small.png", "xcom/00.png"),
      ScreenShot("xcom/01_small.png", "xcom/01.png"),
      ScreenShot("xcom/02_small.png", "xcom/02.png"),
      ScreenShot("xcom/03_small.jpg", "xcom/03.jpg"))

}

@GET("/:lang/xcom/story")
class XcomStory extends DefaultLayout {

  def title           = "НЛО прилетело и опубликовало себя в браузере"
  def description     = "Пронзительно гудела сирена, коридор полыхал от огня тревожных фонарей. Скайрейнджер был уже над землей. Отважные бойцы X-COM, на самом деле простые ребята из разных уголков планеты летели на свое первое задание."
  def keywords        = "фантастический рассказ"

  def execute = {
    respondView()
  }

}

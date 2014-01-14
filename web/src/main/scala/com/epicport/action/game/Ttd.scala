package com.epicport.action.game

import xitrum.annotation.GET
import com.epicport.action.core.Link

@GET("/:lang/ttd/description")
class Ttd extends GameDescription {

  def title           = t("html_page_description_ttd_name")
  def description     = t("html_page_description_ttd_description_short")
  def keywords        = t("html_page_description_ttd_keywords")
  def gameName        = t("html_page_description_ttd_name")
  def gameDescription = t("html_page_description_ttd_description")
  def linkToPlay      = s" http://${getLanguage}.play-ttd.com"
  def links           = Seq()

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("ttd/00_small.png", "ttd/00.png"),
      ScreenShot("ttd/01_small.png", "ttd/01.png"),
      ScreenShot("ttd/02_small.png", "ttd/02.png"),
      ScreenShot("ttd/03_small.png", "ttd/03.png"))


}
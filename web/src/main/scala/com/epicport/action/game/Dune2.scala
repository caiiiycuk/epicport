package com.epicport.action.game

import com.epicport.action.core.Link
import com.epicport.action.core.StaticPageLink
import xitrum.Action
import xitrum.annotation.GET
import com.epicport.action.DefaultLayout

@GET("/:lang/dune2/browser")
class Dune2 extends GameLayout {

  def title = t("html_play_dune2_title")
  def description = t("html_play_dune2_description")
  def keywords = t("html_play_dune2_keywords")

  def execute() {
    respondView()
  }

}

@GET("/:lang/dune2/description")
class Dune2DescriptionRedircet extends com.epicport.action.Redirect301[Dune2Description]

@GET("/:lang/dune2")
class Dune2Description extends GameDescriptionV2 {
  def game = Game.DUNE2_BROWSER
  def downloadSizeInMb = 4
  def mainImageUrl = publicUrl("v2/img/dune2-main-image.jpg")
  
  lazy val androidVersion = Link(t("html_play_on_phone"),
    url[Dune2Android]("lang" -> language),
    "new-badge-link")

  def title = t("html_page_description_dune2_name")
  def description = t("html_page_description_dune2_description_short")
  def keywords = t("html_page_description_dune2_keywords")
  def gameName = t("html_page_description_dune2_name")
  def gameDescription = t("html_page_description_dune2_description")
  def linkToPlay = Link(t("html_play_in_browser"), url[Dune2]("lang" -> language))
  def links = language match {
    case "ru" => Seq(androidVersion, StaticPageLink("dune2-story"))
    case _ => Seq(androidVersion)
  }

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("dune2/00_small.png", "dune2/00.png"),
      ScreenShot("dune2/01_small.png", "dune2/01.png"),
      ScreenShot("dune2/02_small.png", "dune2/02.png"),
      ScreenShot("dune2/03_small.png", "dune2/03.png"))

}

@GET("/:lang/dune2/android")
class Dune2Android extends GameDescriptionV2 {
  def game = Game.DUNE2_ANDROID
  def downloadSizeInMb = 20
  def mainImageUrl = publicUrl("v2/img/dune2-main-image.jpg")
  
  def title = t("html_page_dune2_android_title")
  def description = t("html_page_dune2_description")
  def keywords = t("html_page_dune2_keywords")
  
  def gameName: String = title

  def gameDescription: String = description
  
  def linkToPlay = Link(t("android_download") + " Dune2.apk", publicUrl("android/dune2/dune2.apk"))
  
  def links: Seq[Link] = Seq.empty

  def screenshots: Seq[ScreenShot] =
    Seq(ScreenShot("dune2/android_00_small.png", "dune2/android_00.png"),
      ScreenShot("dune2/android_01_small.png", "dune2/android_01.png"),
      ScreenShot("dune2/android_02_small.png", "dune2/android_02.png"),
      ScreenShot("dune2/android_03_small.png", "dune2/android_03.png"),
      ScreenShot("dune2/android_04_small.png", "dune2/android_04.png"),
      ScreenShot("dune2/android_05_small.png", "dune2/android_05.png"))
}
package com.epicport.action.game

import xitrum.Action

case class Game(
    name: String,
    logo: String,
    url: String,
    urlTitle: String,
    mainImageUrl: String,
    wikilikeText: String,
    fts: String = "") {

  def autoFts() = {
    copy(fts = List(fts, name, wikilikeText).mkString(" "))
  }
}

object Game {

  def DUNE2_ANDROID(implicit action: Action) = {
    import action._
    Game(
      name = action.t("Dune 2 - Android"),
      logo = publicUrl("v2/img/dune2-icon-50x50.png"),
      url = url[Dune2Android]("lang" -> language),
      urlTitle = t("index-dune2-android"),
      mainImageUrl = publicUrl("v2/img/dune2-android-main-image.jpg"),
      wikilikeText = t("index-dune2-section"),
      fts = "2 dune дюна dune2 дюна2").autoFts()
  }

  def DUNE2_BROWSER(implicit action: Action) = {
    import action._
    Game(
      name = action.t("Dune 2 - Browser"),
      logo = publicUrl("v2/img/dune2-icon-50x50.png"),
      url = url[Dune2Description]("lang" -> language),
      urlTitle = t("index-dune2-browser"),
      mainImageUrl = publicUrl("v2/img/dune2-main-image.jpg"),
      wikilikeText = t("index-dune2-section"),
      fts = "2 dune дюна dune2 дюна2 dune 2 дюна 2")
  }

  def TTD(implicit action: Action) = {
    import action._
    Game(
      name = action.t("TTD"),
      logo = publicUrl("v2/img/openttd-icon-50x50.png"),
      url = url[TtdSinglePlayer]("lang" -> language),
      urlTitle = t("index-ttd-browser"),
      mainImageUrl = publicUrl("v2/img/ttd-main-image.jpg"),
      wikilikeText = t("index-ttd-section"),
      fts = "transport tycoon deluxe транспортный магнат").autoFts()
  }

  def XCOM(implicit action: Action) = {
    import action._
    Game(
      name = action.t("X-Com"),
      logo = publicUrl("v2/img/xcom-icon-50x50.png"),
      url = url[XcomDescription]("lang" -> language),
      urlTitle = t("index-xcom-browser"),
      mainImageUrl = publicUrl("v2/img/xcom-main-image.jpg"),
      wikilikeText = t("index-xcom-section"),
      fts = "xcom x-com ufo enemy unknown").autoFts()
  }

  def CAESAR3(implicit action: Action) = {
    import action._
    Game(
      name = action.t("Caesar III"),
      logo = publicUrl("v2/img/caesar-icon-50x50.png"),
      url = url[CaesariaDescription]("lang" -> language),
      urlTitle = t("index-cesaria-browser"),
      mainImageUrl = publicUrl("v2/img/caesar-main-image.jpg"),
      wikilikeText = t("index-cesaria-section"),
      fts = "caesar цезарь").autoFts()
  }

  def all(implicit action: Action): Seq[Game] = {
    Seq(DUNE2_BROWSER, DUNE2_ANDROID, TTD, XCOM, CAESAR3)
  }

}
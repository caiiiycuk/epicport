package com.epicport.action.game

import xitrum.Action

case class Game(name: String, logo: String, url: String)

object Game {

  def DUNE2_ANDROID(implicit action: Action) = {
    import action._
    Game(action.t("Dune 2 - Android"), publicUrl("v2/img/dune2-icon-50x50.png"), url[Dune2Android]("lang" -> language))
  }

  def DUNE2_BROWSER(implicit action: Action) = {
    import action._
    Game(action.t("Dune 2 - Browser"), publicUrl("v2/img/dune2-icon-50x50.png"), url[Dune2Description]("lang" -> language))
  }

  def TTD(implicit action: Action) = {
    import action._
    Game(action.t("TTD"), publicUrl("v2/img/openttd-icon-50x50.png"), url[TtdDescription]("lang" -> language))
  }

  def XCOM(implicit action: Action) = {
    import action._
    Game(action.t("X-Com"), publicUrl("v2/img/xcom-icon-50x50.png"), url[XcomDescription]("lang" -> language))
  }

  def CAESAR3(implicit action: Action) = {
    import action._
    Game(action.t("Caesar III"), publicUrl("v2/img/caesar-icon-50x50.png"), url[CaesariaDescription]("lang" -> language))
  }

  def all(implicit action: Action):Seq[Game] = {
    Seq(DUNE2_ANDROID, DUNE2_BROWSER, TTD, XCOM, CAESAR3)
  }

}
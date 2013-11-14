package com.epicport.action.xhr.save

import scala.annotation.meta.param
import scala.annotation.meta.param
import xitrum.Action
import java.io.File

trait SavesXhr extends Action {

  import com.epicport.Configuration._

  lazy val player = param("id")
  lazy val game = param("game")

  def lefacyFolder(game: String, player: String) =
    new File(new File(new File(data, game), "save"), player)
}
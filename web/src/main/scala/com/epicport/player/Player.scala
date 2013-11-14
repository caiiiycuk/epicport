package com.epicport.player

import java.util.Date
import xitrum.Action
import java.text.SimpleDateFormat

case class Player(
  id: String,
  createdAt: Date)

object Player {
  
  val dateFormat = new SimpleDateFormat("YYYY.mm.DD")

//  def forAction(action: Action): Option[Player] = {
//    for (
//      id <- action.paramo("id").getOrElse(action.requestCookies.get("player_id"));
//      createdAt <- action.requestCookies.get("player_createdAt")
//    ) yield Player(id, dateFormat.parse(createdAt))
//  }

}
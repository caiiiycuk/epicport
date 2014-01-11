package com.epicport.action.xhr.storage

import com.epicport.action.NotFoundError
import com.epicport.player.Profile

import xitrum.Action
import xitrum.SkipCsrfCheck
import xitrum.annotation.Error404
import xitrum.scope.session.Csrf

trait Storage extends Action with SkipCsrfCheck {

  lazy val profile =
    Option(handlerEnv.request.headers.get("X-Profile")).map(Profile.fromJson(_)).getOrElse(
      Profile.fromIdentity(param("identity")))

  lazy val game =
    Option(handlerEnv.request.headers.get("X-Game")).getOrElse(param("game"))
      
  lazy val fileName =
    Option(handlerEnv.request.headers.get("X-File-Name")).getOrElse(param("fileName")).replaceAll("\\P{InBasic_Latin}", "?")

}
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

  beforeFilter {
    if (!isValidToken(Storage.this)) {
      forwardTo[NotFoundError]()
      false
    } else {
      true
    }
  }

  private def isValidToken(action: Action): Boolean = {
    if (action.request.getMethod.getName == "GET") {
      return true
    }

    // The token must be in the request body for more security
    val bodyParams = action.handlerEnv.bodyParams
    val headers = action.handlerEnv.request.headers
    val tokenInRequest = Option(headers.get(s"X-${Csrf.TOKEN}"))
      .getOrElse(action.param(Csrf.TOKEN, bodyParams))

    // Cleaner for application developers when seeing access log
    bodyParams.remove(Csrf.TOKEN)

    val tokenInSession = action.antiCsrfToken
    tokenInRequest == tokenInSession
  }

}
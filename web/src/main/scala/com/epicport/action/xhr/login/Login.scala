package com.epicport.action.xhr.login

import com.epicport.db.Db
import com.epicport.db.User

import xitrum.FutureAction
import xitrum.SkipCsrfCheck
import xitrum.annotation.POST

import io.netty.handler.codec.http.HttpResponseStatus

@POST("/xhr/login")
class Login extends FutureAction with Db {

  def execute() = db.withDynSession {
    val email = param("email")
    val password = param("password")

    User.byEmail(email.toLowerCase, password) match {
      case Some(user) =>
        respondText(user.toJson, "application/json")
      case _ => {
        response.setStatus(HttpResponseStatus.NOT_FOUND)
        respond()
      }
    }
  }

}
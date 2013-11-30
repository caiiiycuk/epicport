package com.epicport.action.xhr.login

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import com.epicport.db.Db
import com.epicport.db.User
import xitrum.Action
import xitrum.annotation.POST
import xitrum.SkipCsrfCheck

@POST("/xhr/login")
class Login extends Action with Db {

  def execute() = db.withSession {
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
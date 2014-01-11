package com.epicport.action

import xitrum.annotation.GET
import com.epicport.db.Db
import xitrum.Action
import xitrum.annotation.POST
import com.epicport.db.User
import io.netty.handler.codec.http.DefaultCookie
import java.net.URLEncoder

@GET("/:lang/register")
class Register extends DefaultLayout {
  def title = t("html_register_title")
  def description = t("html_register_description")
  def keywords = t("html_register_keywords")

  def execute() {
    respondView()
  }
}

@POST("/:lang/register")
class NewRegistration extends Action with Db {

  def execute() {
    val email = paramo("email")
    val name = paramo("name")
    val password = paramo("password")
    val confirmPassword = paramo("confirm_password")

    val fields = Seq(email, name, password, confirmPassword).flatten.toSeq
    
    if (fields.isEmpty || fields.find(_.length == 0).isDefined) {
      redirectTo[Register]("lang" -> param("lang"),
        "error" -> "html_required_fields_not_set")
    } else {
      register(email.get.toLowerCase, name.get, password.get, confirmPassword.get)
    }
  }

  def register(email: String, name: String, password: String, confirmPassword: String): Unit = {
    if (password != confirmPassword) {
      redirectTo[Register]("lang" -> param("lang"),
        "email" -> email,
        "name" -> name,
        "error" -> "html_passwords_not_match")
      return
    }

    db.withTransaction {
      if (User.byEmail(email).isDefined) {
        redirectTo[Register]("lang" -> param("lang"),
          "email" -> email,
          "name" -> name,
          "error" -> "html_alredy_exists")
        return
      }

      val created = User.create(email, name, password)
      val encoded = URLEncoder.encode(created.toJson, "UTF-8")
      val cookie = new DefaultCookie("profile", encoded)
      responseCookies.append(cookie)

      redirectTo[I18NRoot]("lang" -> param("lang"))
    }
  }

}
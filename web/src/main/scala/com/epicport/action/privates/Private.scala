package com.epicport.action.privates

import xitrum.annotation.GET
import com.epicport.secret.Secret
import com.epicport.action.EmptyLayout
import xitrum.Config

trait PrivateAction extends EmptyLayout {

  beforeFilter {
    val isValidSecret = !Config.productionMode ||
      Secret.decodeSecret(paramo("secret"))

    if (!isValidSecret) {
      respondDefault404Page()
    }

    isValidSecret
  }

  def title = t("html_private_page_title")
  def description = t("html_private_page_description")
  def keywords = t("html_private_page_keywords")

  val game: String

  def execute() {
    at("game") = game
    respondView()
  }
}

@GET("/:lang/private/wargus/")
class WargusPrivateAction extends PrivateAction {
  val game = "wargus"
}
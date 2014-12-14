package com.epicport.action

import xitrum.annotation.GET
import xitrum.FutureAction
import com.epicport.i18n.I18N
import io.netty.handler.codec.http.HttpResponseStatus

@GET("")
class Root extends FutureAction {
  def execute() {
    redirectTo(url[I18NRoot]("lang" -> "en"), HttpResponseStatus.MOVED_PERMANENTLY)
  }
}

@GET("/:lang")
class I18NRoot extends DefaultAction {
  def title = t("html_title")
  def description = t("html_description")
  def keywords = t("html_keywords")

  def execute() {
    respondView()
  }
}

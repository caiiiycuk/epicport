package com.epicport.action

import xitrum.annotation.GET
import xitrum.FutureAction
import com.epicport.i18n.I18N
import io.netty.handler.codec.http.HttpResponseStatus
import xitrum.SkipCsrfCheck

@GET("")
class Root extends FutureAction with SkipCsrfCheck  {
  import I18N._
  
  def execute() {
    autosetLanguage(languages.toSeq: _*)
    redirectTo(url[I18NRoot]("lang" -> language))
  }
}

@GET("/:lang<^en$|^ru$>")
class I18NRoot extends DefaultAction {
  def title = t("html_title")
  def description = t("html_description")
  def keywords = t("html_keywords")

  def execute() {
    respondView()
  }
}

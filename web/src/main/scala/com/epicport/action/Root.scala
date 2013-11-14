package com.epicport.action

import xitrum.annotation.GET
import xitrum.Action
import com.epicport.i18n.I18N

@GET("")
class Root extends Action {
  import I18N._
  
  def execute() {
    autosetLanguage(languages.toSeq: _*)
    redirectTo[I18NRoot]("lang" -> getLanguage)
  }
}

@GET("/:lang")
class I18NRoot extends DefaultLayout {
  def title = t("html_title")
  def description = t("html_description")
  def keywords = t("html_keywords")

  def execute() {
    respondView()
  }
}

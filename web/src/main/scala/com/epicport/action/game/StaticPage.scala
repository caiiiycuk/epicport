package com.epicport.action.game

import com.epicport.action.DefaultLayout
import xitrum.annotation.GET
import com.epicport.action.I18NRoot

@GET("/:lang/static/:page")
class StaticPage extends DefaultLayout {

  lazy val page = param("page")
  
  beforeFilter {
    if (title == s"html_static_page_title_$page") {
	    redirectTo[I18NRoot]("lang" -> param("lang"))  
	    false  
    } else {
      true
    }
  }

  def title       = t(s"html_static_page_title_$page")
  def description = t(s"html_static_page_description_$page")
  def keywords    = t(s"html_static_page_keywords_$page")

  def execute = {
    at("fragment") = renderFragment(page)
    respondView()
  }
  
  override def i18nSupport = false

}
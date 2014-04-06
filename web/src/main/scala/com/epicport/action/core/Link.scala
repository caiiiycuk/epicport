package com.epicport.action.core

import xitrum.Action
import com.epicport.action.game.StaticPage

case class Link(title: String, url: String, linkClass: String = "")

object StaticPageLink {
  
  def apply(page: String)(implicit action: Action) = 
    Link(action.t(s"html_static_page_title_$page"), 
        action.url[StaticPage]("lang" -> action.language,
            "page" -> page), 
        "default-link")
  
}
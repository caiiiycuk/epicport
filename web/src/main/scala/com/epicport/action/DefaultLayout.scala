package com.epicport.action

import xitrum.Action
import com.epicport.i18n.I18N

trait DefaultAction extends Action {
  import I18N._

  def title: String
  def description: String
  def keywords: String

  beforeFilter {
    paramo("lang") match {
      case Some(lang) if languages.contains(lang) =>
        setLanguage(lang)
        true
      case _ =>
        autosetLanguage(languages.toSeq: _*)
        if (!currentAction.isInstanceOf[NotFoundError]) {
          forwardTo[NotFoundError]()
          false
        } else {
          true
        }
    }
  }
}

trait DefaultLayout extends DefaultAction {
  override def layout = renderViewNoLayout[DefaultLayout]()
}

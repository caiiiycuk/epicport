package com.epicport.action

import xitrum.Action
import com.epicport.i18n.I18N
import io.netty.handler.codec.http.HttpResponseStatus
import xitrum.FutureAction
import xitrum.SkipCsrfCheck

trait DefaultAction extends FutureAction with RenderHelper with SkipCsrfCheck {
  import I18N._

  def title: String
  def description: String
  def keywords: String

  beforeFilter {
    val uri = request.getUri
    
    if (uri.endsWith("/")) {
      redirectTo(uri.dropRight(1), HttpResponseStatus.MOVED_PERMANENTLY)
      false
    } else {
      paramo("lang") match {
        case Some(lang) if languages.contains(lang) =>
          language = lang
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

  def i18nSupport = true
  def adsByGoogle = true
}

trait DefaultLayout extends DefaultAction {
  override def layout = renderViewNoLayout[DefaultLayout]()
}

trait EmptyLayout extends DefaultAction {
  override def layout = renderViewNoLayout[EmptyLayout]()
}

package com.epicport.action

import xitrum.Action
import xitrum.FutureAction
import io.netty.handler.codec.http.HttpResponseStatus

class Redirect301[T <: Action: Manifest] extends FutureAction {

  beforeFilter {
    redirectTo(url[T]("lang" -> param("lang")), HttpResponseStatus.MOVED_PERMANENTLY)
    false
  }

  def execute = {
    throw new UnsupportedOperationException
  }

}
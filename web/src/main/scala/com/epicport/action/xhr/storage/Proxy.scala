package com.epicport.action.xhr.storage

import xitrum.annotation.GET
import xitrum.Action
import com.github.caiiiycuk.async4s.dsl.Async4sDSL
import com.epicport.ws.WS
import java.util.concurrent.Future
import scala.concurrent.ExecutionContext
import com.ning.http.client.AsyncHandler
import com.ning.http.client.AsyncHandler.STATE
import com.ning.http.client.HttpResponseBodyPart
import com.ning.http.client.HttpResponseHeaders
import com.ning.http.client.HttpResponseStatus

//@GET("/storage/proxy")
class Proxy extends Action with WS {
  import Async4sDSL._
  
  def execute() {
    val url = param("url")
    
    httpClient.prepareGet(url).execute(new AsyncHandler[Unit]() {

      def onStatusReceived(s: HttpResponseStatus): STATE = STATE.CONTINUE 
      
      def onHeadersReceived(h: HttpResponseHeaders): STATE = {
        response.headers().add("Content-Length", h.getHeaders().getFirstValue("Content-Length"))
        response.headers().add("Content-Type", h.getHeaders().getFirstValue("Content-Type"))
        response.setChunked(true)
        STATE.CONTINUE
      }
      
      def onBodyPartReceived(bodyPart: HttpResponseBodyPart): STATE = {
        respondBinary(bodyPart.getBodyPartBytes)
        STATE.CONTINUE
      }
      
      def onCompleted(): Unit = {
        respondLastChunk
      }
      
      def onThrowable(t: Throwable): Unit = {
        respondLastChunk
      }

    })
  }

}
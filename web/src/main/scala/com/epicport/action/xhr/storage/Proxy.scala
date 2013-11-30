package com.epicport.action.xhr.storage

import xitrum.annotation.GET
import xitrum.Action
import com.github.caiiiycuk.async4s.dsl.Async4sDSL
import com.epicport.ws.WS
import java.util.concurrent.Future
import scala.concurrent.ExecutionContext

@GET("/storage/proxy")
class Proxy extends Action with WS {
  import Async4sDSL._
  
  def execute() {
    val url = param("url")
    val future = async_get(url as BYTES)
    
    future.addListener(new Runnable() {
      def run() {
        respondBinary(future.get)
      }
    }, ExecutionContext.Implicits.global)
  }
  

}
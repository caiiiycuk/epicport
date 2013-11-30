package com.epicport.ws

import com.ning.http.client.AsyncHttpClientConfig
import com.ning.http.client.AsyncHttpClient

private object HttpClient {
      private val configBuilder = new AsyncHttpClientConfig.Builder()

      configBuilder.setAllowPoolingConnection(true)
      configBuilder.setMaximumConnectionsTotal(100)
      configBuilder.setConnectionTimeoutInMs(3000)
      configBuilder.setRequestTimeoutInMs(3000)
      configBuilder.setFollowRedirects(true)

      val client = new AsyncHttpClient(configBuilder.build())
      def close = client.close
    }

trait WS {
	implicit val httpClient = HttpClient.client
}
package com.playground.service

import akka.actor.{Actor, ActorLogging}
import java.util.concurrent.{Executor, Executors}
import com.playground.Application
import com.playground.service.WebClientService.{Command, Result}
import com.ning.http.client.{AsyncCompletionHandler, AsyncHttpClient, AsyncHttpClientConfig, Response}
import scala.concurrent.{Future, Promise}

object WebClientService {
  object Command {
    case class Get(url: String)
  }
  object Result {
    case class Success(url: String, body: String)
    case class Failure(url: String, throwable: Throwable)
  }
}

class WebClientService(implicit val application: Application) extends Actor with ActorLogging {
  val config = new AsyncHttpClientConfig.Builder()
  val client = new AsyncHttpClient(
    config
      .setFollowRedirect(true)
      .setExecutorService(Executors.newWorkStealingPool(64))
      .build()
  )

  def receive = {
    case Command.Get(url) =>
      val sender = context.sender

      val request = client.prepareGet(url).build()
      client.executeRequest(request, new AsyncCompletionHandler[Response]() {
        override def onCompleted(response: Response): Response = {
          sender ! Result.Success(url, response.getResponseBody)
          response
        }
        override def onThrowable(t: Throwable): Unit = {
          sender ! Result.Failure(url, t)
        }
      })
  }
}
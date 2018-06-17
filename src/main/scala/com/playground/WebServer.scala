package com.playground

import akka.http.scaladsl.Http
import akka.stream.scaladsl.{Sink, Source}
import com.playground.context.{ActorContextComponentImpl, ConfigContextComponentImpl}
import com.playground.route.MailRoute
import scala.concurrent.Future

class WebServer extends ConfigContextComponentImpl with ActorContextComponentImpl {
  import actorContext._
  import configContext.config

  val routes = MailRoute.routes

  val interface = config.getString("http.interface")
  val port = config.getInt("http.port")

  val httpService = Http()
  val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] = httpService.bind(interface, port)
  val binding: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach { connection =>
      println("Accepted new connection from {}", connection.remoteAddress)
      connection handleWith routes
    }).run()

  println(s"Server is now online at http://$interface:$port\n")

  sys.addShutdownHook {
    println("Terminating...")

    binding.flatMap(_.unbind()).value
    println("Terminated... Bye")
  }
}

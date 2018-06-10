package com.playground

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import com.playground.route.MailRoute
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContextExecutor

object Main extends App {
  implicit val config: Config = ConfigFactory.load(s"application-${sys.env("env")}.conf")
  implicit val logger: Logger = LoggerFactory.getLogger(Main.getClass)

  implicit val system: ActorSystem = ActorSystem("Playground")
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = ActorMaterializer()

  println(s"Current time = ${System.currentTimeMillis}")

  val routes = MailRoute.routes
  Http().bindAndHandle(
    routes,
    config.getString("http.interface"),
    config.getInt("http.port")
  )
}

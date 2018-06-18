package com.playground

import akka.actor.Props
import com.playground.context._
import com.playground.service.MailService
import com.playground.repository.MailRepository
import scala.concurrent.duration._
import scala.concurrent.Await

object Boot extends App {
  println(s"Current time = ${System.currentTimeMillis}")

  implicit val application: Application = new DefaultApplication
  
  import application.actorContext._

  val server = new WebServer

  sys.addShutdownHook {
    val future = system.terminate()
    Await.result(future, 60.seconds)
  }
}

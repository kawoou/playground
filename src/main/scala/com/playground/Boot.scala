package com.playground

import com.playground.context.{ActorContextComponentImpl, ConfigContextComponentImpl}
import scala.concurrent.duration._
import scala.concurrent.Await

object Boot extends App with ConfigContextComponentImpl with ActorContextComponentImpl {
  println(s"Current time = ${System.currentTimeMillis}")

  val server = new WebServer()

  sys.addShutdownHook {
    val future = actorContext.system.terminate()
    Await.result(future, 60.seconds)
  }
}

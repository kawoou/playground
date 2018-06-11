package com.playground

import com.playground.route.MailRoute

import akka.http.scaladsl.Http

object Main extends App with ApplicationComponentImpl {
  import application._

  println(s"Current time = ${System.currentTimeMillis}")

  val routes = MailRoute.routes
  Http().bindAndHandle(
    routes,
    config.getString("http.interface"),
    config.getInt("http.port")
  )
}

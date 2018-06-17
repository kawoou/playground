package com.playground.route.actor

import akka.actor.{Actor, ActorLogging, ReceiveTimeout}
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes.{GatewayTimeout, InternalServerError}
import akka.http.scaladsl.server.{RequestContext, RouteResult}

import scala.concurrent.Promise

trait AbstractRouteActor extends Actor with ActorLogging {
  implicit def request: RequestContext
  val promise: Promise[RouteResult] = Promise[RouteResult]

  import context._

  def complete(m: => ToResponseMarshallable): Unit = {
    val f = request.complete(m)
    f.onComplete(promise.complete)
    context.stop(self)
  }

  def receive = {
    case ReceiveTimeout =>
      complete(GatewayTimeout, "Request timeout")

    case event =>
      log.error(s"Cannot support `$event` event on ${self.path}")
      complete(InternalServerError, s"Cannot support `$event` event on ${self.path}")
  }
}
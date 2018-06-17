package com.playground.route

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}
import com.playground.route.actor.AbstractRouteActor

import scala.concurrent.Promise

trait AbstractRoute {
  def routes: Route

  def actorRoute(factory: (RequestContext) => AbstractRouteActor)(implicit system: ActorSystem): Route = ctx => {
    val promise = Promise[RouteResult]
    system.actorOf(Props(factory(ctx)))
    promise.future
  }
}

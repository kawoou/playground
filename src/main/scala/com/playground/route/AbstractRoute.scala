package com.playground.route

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}
import com.playground.Application
import com.playground.route.actor.AbstractRouteActor

import scala.concurrent.Promise

trait AbstractRoute {
  def application: Application
  def routes: Route

  def actorRoute(factory: (RequestContext, Promise[RouteResult]) => AbstractRouteActor)(implicit system: ActorSystem): Route = ctx => {
    val promise = Promise[RouteResult]
    val actor = system.actorOf(Props(factory(ctx, promise)))
    promise.future
  }
}

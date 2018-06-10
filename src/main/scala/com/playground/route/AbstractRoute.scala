package com.playground.route

import akka.http.scaladsl.server.Route

trait AbstractRoute {
  def routes: Route
}

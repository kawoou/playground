package com.playground.route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.playground.context.ActorContextComponentImpl
import com.playground.route.actor.{ListMailActor, SendMailActor, SendMailRequest}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object MailRoute extends AbstractRoute with ActorContextComponentImpl with DefaultJsonProtocol with SprayJsonSupport {
  import actorContext._

  // Private
  private implicit val mailSendRequestFormat: RootJsonFormat[SendMailRequest] = jsonFormat3(SendMailRequest.apply)

  // Public
  val routes: Route = pathPrefix("mail") {
    pathEnd {
      (get & parameter("page" ? "0")) { page =>
        actorRoute {
          new ListMailActor(page.toInt, 20, _)
        }
      } ~
      (post & entity(as[SendMailRequest])) { request =>
        actorRoute {
          new SendMailActor(request, _)
        }
      }
    }
  }
}
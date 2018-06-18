package com.playground.route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.playground.Application
import com.playground.route.actor.{ListMailActor, SendMailActor, SendMailRequest}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

class MailRoute(implicit val application: Application) extends AbstractRoute with DefaultJsonProtocol with SprayJsonSupport {
  import application.actorContext._

  // Private
  private implicit val mailSendRequestFormat: RootJsonFormat[SendMailRequest] = jsonFormat3(SendMailRequest.apply)

  // Public
  val routes: Route = pathPrefix("mail") {
    pathEnd {
      (get & parameter("page" ? "0")) { page =>
        actorRoute {
          new ListMailActor(page.toInt, 20, _, _)
        }
      } ~
      (post & entity(as[SendMailRequest])) { request =>
        actorRoute {
          new SendMailActor(request, _, _)
        }
      }
    }
  }
}
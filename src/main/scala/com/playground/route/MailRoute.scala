package com.playground.route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.playground.entity.MailSendRequest
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object MailRoute extends AbstractRoute with DefaultJsonProtocol with SprayJsonSupport {
  implicit val mailSendRequest: RootJsonFormat[MailSendRequest] = jsonFormat3(MailSendRequest.apply)

  val routes: Route = pathPrefix("mail") {
    pathEnd {
      (get & parameter("page" ? "0")) { page =>
        complete {
          ""
        }
      } ~
      (post & entity(as[MailSendRequest])) { mailSendRequest =>
        complete {
          mailSendRequest.name
        }
      }
    }
  }
}

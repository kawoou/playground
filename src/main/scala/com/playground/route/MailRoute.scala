package com.playground.route

import com.playground.ApplicationComponentImpl
import com.playground.entity.MailSendRequest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import scala.util.{Failure, Success}

object MailRoute extends AbstractRoute with DefaultJsonProtocol with SprayJsonSupport with ApplicationComponentImpl {
  implicit val mailSendRequest: RootJsonFormat[MailSendRequest] = jsonFormat3(MailSendRequest.apply)

  val routes: Route = pathPrefix("mail") {
    pathEnd {
      (get & parameter("page" ? "0")) { page =>
        onComplete(application.mailService.getList(0, 20)) {
          case Success(value) => complete(s"${value}")
          case Failure(ex) => complete((StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}"))
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

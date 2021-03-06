package com.playground.route.actor

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import com.playground.Application
import com.playground.repository.MailRepository
import com.playground.service.MailService

import scala.concurrent.duration._
import scala.concurrent.Promise

class DeleteMailActor(id: Long, password: String, val request: RequestContext, val promise: Promise[RouteResult])(implicit val application: Application) extends AbstractRouteActor {
  context.setReceiveTimeout(2.seconds)

  application.mailService ! MailService.Command.GetByID(id, self)

  override def receive = {
    case MailRepository.Result.Get(mail) =>
      if (mail.isDefined) {
        if (mail.get testPassword password) {
          application.mailService ! MailService.Command.Delete(id, Option(self))
        } else {
          complete(Unauthorized)
        }
      } else {
        complete(BadRequest)
      }

    case MailRepository.Result.Deleted(id) =>
      complete(OK, id.toString())

    case _ => super.receive
  }
}

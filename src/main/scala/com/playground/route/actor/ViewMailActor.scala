package com.playground.route.actor

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import com.playground.Application
import com.playground.repository.MailRepository
import com.playground.service.MailService

import scala.concurrent.duration._
import scala.concurrent.Promise

class ViewMailActor(id: Long, val request: RequestContext, val promise: Promise[RouteResult])(implicit val application: Application) extends AbstractRouteActor {
  context.setReceiveTimeout(2.seconds)

  application.mailService ! MailService.Command.GetByID(id, self)

  override def receive = {
    case MailRepository.Result.Get(mail) =>
      complete(OK, mail.toString())

    case _ => super.receive
  }
}

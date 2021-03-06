package com.playground.route.actor

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import com.playground.Application
import com.playground.repository.MailRepository
import com.playground.service.MailService

import scala.concurrent.duration._
import scala.concurrent.Promise

class ListMailActor(page: Int, count: Int, val request: RequestContext, val promise: Promise[RouteResult])(implicit val application: Application) extends AbstractRouteActor {
  context.setReceiveTimeout(2.seconds)

  application.mailService ! MailService.Command.Gets(page, 20, self)

  override def receive = {
    case MailRepository.Result.Gets(mails) =>
      complete(OK, mails.toString())

    case _ => super.receive
  }
}
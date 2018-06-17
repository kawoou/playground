package com.playground.route.actor

import java.util.concurrent.TimeUnit

import akka.actor.Props
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.RequestContext
import com.playground.service.MailService
import scala.concurrent.duration._

class ListMailActor(page: Int, count: Int, val request: RequestContext) extends AbstractRouteActor {
  context.setReceiveTimeout(2.seconds)

  private val mailService = context.actorOf(Props[MailService], "MailService")
  mailService ! MailService.Command.Gets(page, 20)

  override def receive = {
    case MailService.Result.Gets(mails) =>
      complete(OK, mails.toString())

    case _ => super.receive
  }
}
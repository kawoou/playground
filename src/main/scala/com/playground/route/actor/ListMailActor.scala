package com.playground.route.actor

import java.util.concurrent.TimeUnit

import akka.actor.Props
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import com.playground.Application
import com.playground.service.MailService
import scala.concurrent.duration._

import scala.concurrent.Promise

class ListMailActor(page: Int, count: Int, val request: RequestContext, val promise: Promise[RouteResult])(implicit val application: Application) extends AbstractRouteActor {
  context.setReceiveTimeout(2.seconds)

  private val mailService = context.actorOf(Props(new MailService))
  mailService ! MailService.Command.Gets(page, 20)

  override def receive = {
    case MailService.Result.Gets(mails) =>
      println(s"MailService.Result.Gets - sender: $sender")
      complete(OK, mails.toString())

    case _ => super.receive
  }
}
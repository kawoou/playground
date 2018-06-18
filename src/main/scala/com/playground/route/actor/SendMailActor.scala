package com.playground.route.actor

import java.util.concurrent.TimeUnit

import akka.actor.Props
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import com.playground.Application
import com.playground.entity.table.Mail
import com.playground.service.MailService

import scala.concurrent.duration.Duration
import scala.concurrent.Promise

case class SendMailRequest(name: String, title: String, content: String)

class SendMailActor(body: SendMailRequest, val request: RequestContext, val promise: Promise[RouteResult])(implicit val application: Application) extends AbstractRouteActor {
  context.setReceiveTimeout(Duration(2, TimeUnit.SECONDS))

  private val mail = Mail(body.title, body.content, body.name)
  private val mailService = context.actorOf(Props(new MailService))
  mailService ! MailService.Command.Add(mail)

  override def receive = {
    case MailService.Event.Added(id) =>
      complete(OK, id.toString())

    case _ => super.receive
  }
}

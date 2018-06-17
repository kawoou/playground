package com.playground.route.actor

import java.util.concurrent.TimeUnit

import akka.actor.Props
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.RequestContext
import com.playground.entity.table.Mail
import com.playground.service.MailService

import scala.concurrent.duration.Duration

case class SendMailRequest(name: String, title: String, content: String)

class SendMailActor(body: SendMailRequest, val request: RequestContext) extends AbstractRouteActor {
  context.setReceiveTimeout(Duration(2, TimeUnit.SECONDS))

  private val mail = Mail(body.title, body.content, body.name)
  private val mailService = context.actorOf(Props[MailService], "MailService")
  mailService ! MailService.Command.Add(mail)

  override def receive = {
    case MailService.Event.Added(id) =>
      complete(OK, id.toString())

    case _ => super.receive
  }
}

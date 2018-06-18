package com.playground.route.actor

import java.util.concurrent.TimeUnit

import akka.actor.Props
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import com.playground.Application
import com.playground.common.Passwordable
import com.playground.entity.table.Mail
import com.playground.repository.MailRepository
import com.playground.service.MailService

import scala.concurrent.duration.Duration
import scala.concurrent.Promise

case class SendMailRequest(title: String, content: String, name: String, password: String)

class SendMailActor(body: SendMailRequest, val request: RequestContext, val promise: Promise[RouteResult])(implicit val application: Application) extends AbstractRouteActor {
  context.setReceiveTimeout(Duration(2, TimeUnit.SECONDS))

  private val salt = Passwordable.makeSalt
  private val password = Passwordable.encryptPassword(body.password, salt)
  private val mail = Mail(body.title, body.content, body.name, password, salt)

  application.mailService ! MailService.Command.Add(mail, Option(self))

  override def receive = {
    case MailRepository.Result.Added(id) =>
      complete(OK, id.toString())

    case _ => super.receive
  }
}

package com.playground.service

import akka.actor.{Actor, ActorRef, ActorLogging}
import com.playground.Application
import com.playground.entity.table.Mail
import com.playground.repository.MailRepository
import com.playground.service.MailService.{Command, Event}

object MailService {
  object Command {
    case class Gets(page: Int, count: Int, ref: ActorRef)
    case class GetByID(id: Long, ref: ActorRef)
    case class Add(mail: Mail, ref: Option[ActorRef])
    case class Delete(id: Long, ref: Option[ActorRef])
  }
  object Event {
    case class Added(id: Long)
    case class Deleted(id: Long)
  }
}

class MailService(implicit val application: Application) extends Actor with ActorLogging {
  def receive = {
    /// Request
    case Command.Gets(page, count, ref) =>
      application.mailRepository ! MailRepository.Command.Gets(page, count, ref)

    case Command.GetByID(id, ref) =>
      application.mailRepository ! MailRepository.Command.GetByID(id, ref)

    case Command.Add(mail, ref) =>
      application.mailRepository ! MailRepository.Command.Add(mail, ref)

    case Command.Delete(id, ref) =>
      application.mailRepository ! MailRepository.Command.Delete(id, ref)

    /// MailRepository
    case MailRepository.Result.Added(id) =>
      context.system.eventStream publish Event.Added(id)

    case MailRepository.Result.Deleted(id) =>
      context.system.eventStream publish Event.Deleted(id)

    case event =>
      log.error(s"Cannot support `$event` event on ${self.path}")
  }

}

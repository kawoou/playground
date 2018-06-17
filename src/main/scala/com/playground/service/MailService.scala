package com.playground.service

import akka.actor.{Actor, ActorLogging, Props}
import com.playground.context.ActorContextComponentImpl
import com.playground.entity.table.Mail
import com.playground.repository.MailRepository
import com.playground.service.MailService.{Command, Event, Result}

object MailService {
  object Command {
    case class Gets(page: Int, count: Int)
    case class GetByID(id: Long)
    case class Add(mail: Mail)
  }

  object Event {
    case class Added(id: Long)
  }

  object Result {
    case class Gets(mails: List[Mail])
    case class Get(mail: Option[Mail])
  }
}

class MailService extends Actor with ActorLogging with ActorContextComponentImpl {
  import actorContext._

  // Private
  private val mailRepository = system.actorOf(Props[MailRepository], "MailRepository")

  // Public
  def receive = {
    /// Request
    case Command.Gets(page, count) =>
      mailRepository ! MailRepository.Command.Gets(page, count)

    case Command.GetByID(id) =>
      mailRepository ! MailRepository.Command.GetByID(id)

    case Command.Add(mail) =>
      mailRepository ! MailRepository.Command.Add(mail)

    /// MailRepository
    case MailRepository.Result.Gets(mails) =>
      val sender = context.sender
      sender ! Result.Gets(mails)

    case MailRepository.Result.Get(mail) =>
      val sender = context.sender
      sender ! Result.Get(mail)

    case MailRepository.Result.Added(id) =>
      val sender = context.sender
      sender ! Event.Added(id)
      context.system.eventStream publish Event.Added(id)

    case event =>
      log.error(s"Cannot support `$event` event on ${self.path}")
  }

}

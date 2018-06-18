package com.playground.service

import java.util.UUID
import akka.actor.{Actor, ActorRef, ActorLogging, Props}
import com.playground.Application
import com.playground.entity.table.Mail
import com.playground.repository.MailRepository
import com.playground.service.MailService.{Command, Event, Result}
import scala.collection.mutable

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

class MailService(implicit val application: Application) extends Actor with ActorLogging {
  import application.actorContext._

  // Private
  private val mailRepository = system.actorOf(Props(new MailRepository))

  val sendersTable = collection.mutable.Map[String, Set[ActorRef]]()

  // Public
  def receive = {
    /// Request
    case Command.Gets(page, count) =>
      val key = s"GETS_${page}_${count}"
      val senders = sendersTable get key
      if (senders.isEmpty) {
        sendersTable += (key -> Set.empty[ActorRef])
      }
      sendersTable(key) += sender
      
      mailRepository ! MailRepository.Command.Gets(page, count)

    case Command.GetByID(id) =>
      mailRepository ! MailRepository.Command.GetByID(id)

    case Command.Add(mail) =>
      mailRepository ! MailRepository.Command.Add(mail)

    /// MailRepository
    case MailRepository.Result.Gets(page, count, mails) =>
      val key = s"GETS_${page}_${count}"

      sendersTable(key) foreach(_ ! Result.Gets(mails))
      sendersTable -= key

    case MailRepository.Result.Get(id, mail) =>
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

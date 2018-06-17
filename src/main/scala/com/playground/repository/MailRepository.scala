package com.playground.repository

import com.playground.context.{ActorContextComponentImpl, ConfigContextComponentImpl, SlickContextComponentImpl}
import com.playground.entity.table.{Mail, Mails}
import akka.actor.{Actor, ActorLogging}
import akka.stream.scaladsl.Sink
import akka.stream.alpakka.slick.scaladsl.Slick
import com.playground.repository.MailRepository.{Command, Result}
import slick.jdbc.PostgresProfile.api._

object MailRepository {
  object Command {
    case class Gets(page: Int, count: Int)
    case class GetByID(id: Long)
    case class Add(mail: Mail)
  }

  object Result {
    case class Added(id: Long)
    case class Gets(mails: List[Mail])
    case class Get(mail: Option[Mail])
  }
}

class MailRepository extends Actor with ActorLogging with ActorContextComponentImpl with ConfigContextComponentImpl with SlickContextComponentImpl {
  import actorContext._
  import slickContext._

  private val Mails = TableQuery[Mails]

  def receive = {
    case Command.Gets(page, count) =>
      val sender = context.sender
      Slick.source(Mails.sortBy(_.id.desc).drop(page * count).take(count).result)
        .runWith(Sink.seq)
        .map(_.toList)
        .map(Result.Gets)
        .foreach { sender ! _ }

    case Command.GetByID(id) =>
      val sender = context.sender
      Slick.source(Mails.filter(_.id === id).result)
        .runWith(Sink.headOption)
        .map(Result.Get)
        .foreach { sender ! _ }

    case Command.Add(mail) =>
      val sender = context.sender
      session.db.run(Mails returning Mails.map(_.id) += mail)
        .map(Result.Added)
        .foreach { sender ! _ }

    case event =>
      log.error(s"Cannot support `$event` event on ${self.path}")
  }
}

package com.playground.repository

import com.playground.Application
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
    case class Gets(page: Int, count: Int, mails: List[Mail])
    case class Get(id: Long, mail: Option[Mail])
  }
}

class MailRepository(implicit val application: Application) extends Actor with ActorLogging {
  import application.actorContext._
  import application.slickContext._

  private val Mails = TableQuery[Mails]

  try {
    session.db.run(Mails.schema.create)
  } catch {
    case e: Exception =>
      log.error(s"Failed to create `Mails` table.")
  }

  def receive = {
    case Command.Gets(page, count) =>
      val sender = context.sender
      Slick.source(Mails.sortBy(_.id.desc).drop(page * count).take(count).result)
        .runWith(Sink.seq)
        .map(_.toList)
        .map(Result.Gets(page, count, _))
        .foreach(sender.!)

    case Command.GetByID(id) =>
      val sender = context.sender
      Slick.source(Mails.filter(_.id === id).result)
        .runWith(Sink.headOption)
        .map(Result.Get(id, _))
        .foreach(sender.!)

    case Command.Add(mail) =>
      val sender = context.sender
      session.db.run(Mails returning Mails.map(_.id) += mail)
        .map(Result.Added)
        .foreach(sender.!)

    case event =>
      log.error(s"Cannot support `$event` event on ${self.path}")
  }
}

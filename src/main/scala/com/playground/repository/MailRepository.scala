package com.playground.repository

import com.playground.Application
import com.playground.entity.table.{Mail, Mails}
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.stream.scaladsl.Sink
import akka.stream.alpakka.slick.scaladsl.Slick
import com.playground.repository.MailRepository.{Command, Result}
import slick.jdbc.PostgresProfile.api._

object MailRepository {
  object Command {
    case class Gets(page: Int, count: Int, ref: ActorRef)
    case class GetByID(id: Long, ref: ActorRef)
    case class Add(mail: Mail, ref: Option[ActorRef])
    case class Delete(id: Long, ref: Option[ActorRef])
  }

  object Result {
    case class Added(id: Long)
    case class Deleted(id: Long)
    case class Gets(mails: List[Mail])
    case class Get(mail: Option[Mail])
  }
}

class MailRepository(implicit val application: Application) extends Actor with ActorLogging {
  import application.actorContext._
  import application.slickContext._

  private val Mails = TableQuery[Mails]

  try {
    session.db.run(Mails.schema.create).value
  } catch {
    case _: Throwable =>
      log.error(s"Failed to create `Mails` table.")
  }

  def receive = {
    case Command.Gets(page, count, ref) =>
      Slick.source(Mails.sortBy(_.id.desc).drop(page * count).take(count).result)
        .runWith(Sink.seq)
        .map(_.toList)
        .map(Result.Gets)
        .foreach(ref.!)

    case Command.GetByID(id, ref) =>
      Slick.source(Mails.filter(_.id === id).result)
        .runWith(Sink.headOption)
        .map(Result.Get)
        .foreach(ref.!)

    case Command.Add(mail, ref) =>
      val sender = context.sender
      session.db.run(Mails returning Mails.map(_.id) += mail)
        .map(Result.Added)
        .foreach { message =>
          ref.foreach(_ ! message)
          sender ! message
        }

    case Command.Delete(id, ref) =>
      val sender = context.sender
      session.db.run(Mails.filter(_.id === id).delete)
        .map(_ => Result.Deleted(id))
        .foreach { message =>
          ref.foreach(_ ! message)
          sender ! message
        }

    case event =>
      log.error(s"Cannot support `$event` event on ${self.path}")
  }
}

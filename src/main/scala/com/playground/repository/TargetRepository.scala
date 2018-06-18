package com.playground.repository

import com.playground.Application
import com.playground.entity.table.{Target, TargetTable}
import com.playground.repository.TargetRepository.{Command, Event, Result}
import akka.actor.{Actor, ActorLogging}
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.Sink
import slick.jdbc.PostgresProfile.api._

object TargetRepository {
  object Command {
    case class Gets(page: Int, count: Int)
    case class GetByID(id: Long)
    case class Add(target: Target)
  }

  object Event {
    case class Added(id: Long)
  }

  object Result {
    case class Target(target: Option[TargetTable#TableElementType])
    case class Targets(targets: List[TargetTable#TableElementType])
  }
}

class TargetRepository(implicit val application: Application) extends Actor with ActorLogging {
  import application.actorContext._
  import application.slickContext._

  val Targets = TableQuery[TargetTable]

  def receive = {
    case Command.Gets(page, count) =>
      val sender = context.sender
      Slick.source(Targets.sortBy(_.id.desc).drop(page * count).take(count).result)
        .runWith(Sink.seq)
        .map(_.toList)
        .map(Result.Targets)
        .foreach(sender ! _)

    case Command.GetByID(id) =>
      val sender = context.sender
      Slick.source(Targets.filter(_.id === id).result)
        .runWith(Sink.headOption)
        .map(Result.Target)
        .foreach(sender ! _)

    case Command.Add(target) =>
      session.db.run(Targets returning Targets.map(_.id) += target)
        .map(Event.Added)
        .onComplete(context.system.eventStream.publish)

    case event =>
      log.error(s"Unknown event $event")
  }
}

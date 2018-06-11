package com.playground.repository

import com.playground.entity.table.{Mail, MailTableComponent}

import akka.Done
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.alpakka.slick.scaladsl.Slick
import scala.concurrent.Future


trait MailRepository {
  def getList(page: Int, count: Int): Future[List[Mail]]

  def findById(id: Long): Future[Option[Mail]]

  def add(mails: Set[Mail]): Future[Done]
}
class MailRepositoryImpl extends MailRepository with MailTableComponent {
  import application._
  import application.session.profile.api._
  
  def getList(page: Int, count: Int): Future[List[Mail]] =
    Slick.source(Mails.sortBy(_.id.desc).drop(page * count).take(count).result)
      .runWith(Sink.seq)
      .map(_.toList)

  def findById(id: Long): Future[Option[Mail]] =
    Slick.source(Mails.filter(_.id === id).result)
      .runWith(Sink.headOption)

  def add(mails: Set[Mail]): Future[Done] =
    Source(mails)
      .runWith(Slick.sink(parallelism = 4, { mail =>
        Mails += mail
      }))
}

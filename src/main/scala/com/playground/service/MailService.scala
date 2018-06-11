package com.playground.service

import com.playground.ApplicationComponentImpl
import com.playground.entity.table.Mail

import akka.Done
import scala.concurrent.Future

trait MailService {
  def getList(page: Int, count: Int): Future[List[Mail]]

  def findById(id: Long): Future[Option[Mail]]

  def add(mails: Set[Mail]): Future[Done]
}
class MailServiceImpl extends MailService with ApplicationComponentImpl {
  import application.mailRepository

  def getList(page: Int, count: Int): Future[List[Mail]] = mailRepository.getList(page, count)

  def findById(id: Long): Future[Option[Mail]] = mailRepository.findById(id)

  def add(mails: Set[Mail]): Future[Done] = mailRepository.add(mails)
}

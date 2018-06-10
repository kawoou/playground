package com.playground.entity

trait Sender {
  def name: String
}

class CrawlerSender(crawler: CrawlerType.Value) extends Sender {
  val name: String = crawler.toString
}
class UserSender(val name: String) extends Sender {}

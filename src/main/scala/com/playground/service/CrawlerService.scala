package com.playground.service

import akka.actor.{Actor, ActorRef, ActorLogging, Props}
import com.playground.Application
import com.playground.entity.{CrawlerType, NaverNewsCategories}
import com.playground.service.CrawlerService.{Command, Result}
import com.playground.service.crawler.NaverNewsCrawler

object CrawlerService {
  object Command {
    case class Crawl()
  }
  object Result {
    
  }
}

class CrawlerService(implicit val application: Application) extends Actor with ActorLogging {
  import application.actorContext._

  def receive = {
    case Command.Crawl =>
      val actor = system.actorOf(Props(new NaverNewsCrawler))
      actor ! NaverNewsCrawler.Command.Crawl(NaverNewsCategories.IT)

    case NaverNewsCrawler.Result.Result(list) =>
      ""

    case event =>
      log.error(s"Cannot support `$event` event on ${self.path}")
  }
}
package com.playground.service.crawler

import akka.actor.{Actor, ActorRef, ActorLogging}
import java.text.SimpleDateFormat
import java.util.Date
import org.jsoup.Jsoup
import com.playground.Application
import com.playground.entity.NaverNewsCategories
import com.playground.service.WebClientService
import com.playground.service.crawler.NaverNewsCrawler.{Command, Result}
import scala.collection.mutable
import scala.collection.JavaConverters._

object NaverNewsCrawler {
  object Command {
    case class Crawl(category: NaverNewsCategories.Category)
  }
  object Result {
    case class Result(list: List[String])
  }
}
class NaverNewsCrawler(implicit val application: Application) extends Actor with ActorLogging {
  var client: Option[ActorRef] = Option.empty

  def receive = {
    case Command.Crawl(category) =>
      val date = new SimpleDateFormat("yyyyMMdd").format(new Date)
      val url = s"http://m.news.naver.com/rankingList.nhn?sid1=${category.key}&date=$date"
      if (client.isEmpty) {
        application.webClientService ! WebClientService.Command.Get(url)
      }
      client = Option(sender)

    case WebClientService.Result.Success(url, body) =>
      val news = Jsoup.parse(body, url)
        .select(".commonlist_tx_headline")
        .iterator
        .asScala
        .map(_.text)
        .toList

      client foreach (_ ! Result.Result(news))
      client = Option.empty

    case WebClientService.Result.Failure(url, throwable) =>
      client foreach (_ ! Result.Result(List.empty))
      client = Option.empty
  }
}
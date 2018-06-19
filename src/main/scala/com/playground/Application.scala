package com.playground

import akka.actor.{ActorRef, Props}
import com.playground.context._
import com.playground.repository.MailRepository
import com.playground.service.{CrawlerService, MailService, WebClientService}

trait Application
  extends LoggerContextComponent
    with ActorContextComponent
    with ConfigContextComponent
    with SlickContextComponent {
  val crawlerService: ActorRef
  val mailService: ActorRef
  val webClientService: ActorRef

  val mailRepository: ActorRef
}

class DefaultApplication
  extends Application
    with DefaultLoggerContextComponent
    with DefaultActorContextComponent
    with DefaultConfigContextComponent 
    with DefaultSlickContextComponent {

  import actorContext._
  implicit val application: Application = this

  override lazy val crawlerService: ActorRef = system.actorOf(Props(new CrawlerService), "CrawlerService")
  override lazy val mailService: ActorRef = system.actorOf(Props(new MailService), "MailService")
  override lazy val webClientService: ActorRef = system.actorOf(Props(new WebClientService), "WebClientService")

  override lazy val mailRepository: ActorRef = system.actorOf(Props(new MailRepository), "MailRepository")
}

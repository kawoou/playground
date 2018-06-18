package com.playground

import akka.actor.{ActorRef, Props}
import com.playground.context._
import com.playground.repository.MailRepository
import com.playground.service.MailService

trait Application
  extends LoggerContextComponent
    with ActorContextComponent
    with ConfigContextComponent
    with SlickContextComponent {
  val mailService: ActorRef
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

  override lazy val mailService: ActorRef = system.actorOf(Props(new MailService), "MailService")
  override lazy val mailRepository: ActorRef = system.actorOf(Props(new MailRepository), "MailRepository")
}

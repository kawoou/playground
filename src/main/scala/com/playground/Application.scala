package com.playground

import com.playground.repository._
import com.playground.service._

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}
import scala.concurrent.ExecutionContextExecutor

trait ApplicationComponent {
  val application: Application

  trait Application {
    implicit val config: Config
    implicit val logger: Logger

    implicit val system: ActorSystem
    implicit val executor: ExecutionContextExecutor
    implicit val materializer: Materializer

    implicit val session: SlickSession

    val mailRepository: MailRepository
    val mailService: MailService
  }
}
trait ApplicationComponentImpl extends ApplicationComponent {
  val application: Application = Application

  object Application extends super.Application {
    implicit val config: Config = ConfigFactory.load(s"application-${sys.env("env")}.conf")
    implicit val logger: Logger = LoggerFactory.getLogger(Main.getClass)

    implicit val system: ActorSystem = ActorSystem("Playground")
    implicit val executor: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: Materializer = ActorMaterializer()

    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres")

    lazy val mailRepository: MailRepository = new MailRepositoryImpl
    lazy val mailService: MailService = new MailServiceImpl
  }
}
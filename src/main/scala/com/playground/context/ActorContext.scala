package com.playground.context

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.ExecutionContextExecutor

trait ActorContextComponent {
  val actorContext: ActorContext

  trait ActorContext {
    implicit val system: ActorSystem
    implicit val executor: ExecutionContextExecutor
    implicit val materializer: Materializer
  }
}

trait ActorContextComponentImpl extends ActorContextComponent {
  val actorContext: ActorContext = ActorContext

  object ActorContext extends super.ActorContext {
    implicit val system: ActorSystem = ActorSystem("Playground")
    implicit val executor: ExecutionContextExecutor = system.dispatcher
    implicit val materializer: Materializer = ActorMaterializer()
  }
}

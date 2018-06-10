package com.playground.repository

import akka.actor.Actor
import com.playground.`type`.Result
import com.playground.entity.Sender
import com.playground.repository.MailRepositoryInput.send

object MailRepositoryInput {
  case class send(sender: Sender) {}
}
object MailRepositoryOutput {
  case class sendResponse(result: Result[Boolean]) {}
}

class MailRepository extends Actor {
  def receive = {
    case send(sender) =>
      ""
  }
}

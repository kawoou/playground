package com.playground.context

import com.playground.Boot
import org.slf4j.{Logger, LoggerFactory}

trait LoggerContextComponent {
  val loggerContext: LoggerContext

  trait LoggerContext {
    implicit val logger: Logger
  }
}

trait LoggerContextComponentImpl extends LoggerContextComponent {
  val loggerContext: LoggerContext = LoggerContext

  object LoggerContext extends super.LoggerContext {
    implicit val logger: Logger = LoggerFactory.getLogger(Boot.getClass)
  }
}

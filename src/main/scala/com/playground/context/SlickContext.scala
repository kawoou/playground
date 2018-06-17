package com.playground.context

import akka.stream.alpakka.slick.scaladsl.SlickSession

trait SlickContextComponent {
  val slickContext: SlickContext

  trait SlickContext {
    implicit val session: SlickSession
  }
}

trait SlickContextComponentImpl extends SlickContextComponent with ConfigContextComponent {
  val slickContext: SlickContext = SlickContext

  object SlickContext extends super.SlickContext {
    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres", configContext.config)
  }
}

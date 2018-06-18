package com.playground.context

import akka.stream.alpakka.slick.scaladsl.SlickSession

trait SlickContextComponent {
  val slickContext: SlickContext

  trait SlickContext {
    implicit val session: SlickSession
  }
}

trait DefaultSlickContextComponent extends SlickContextComponent {
  self: ConfigContextComponent =>

  override lazy val slickContext: SlickContext = new SlickContext

  class SlickContext extends super.SlickContext {
    import configContext._

    implicit val session: SlickSession = SlickSession.forConfig("slick-postgres", config)
  }
}
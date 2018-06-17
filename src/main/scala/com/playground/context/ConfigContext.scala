package com.playground.context

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigContextComponent {
  val configContext: ConfigContext

  trait ConfigContext {
    implicit val config: Config
  }
}

trait ConfigContextComponentImpl extends ConfigContextComponent {
  val configContext: ConfigContext = ConfigContext

  object ConfigContext extends super.ConfigContext {
    implicit val config: Config = ConfigFactory.load(s"application-${sys.env("env")}.conf")
  }
}

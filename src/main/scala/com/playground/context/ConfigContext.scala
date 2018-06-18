package com.playground.context

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigContextComponent {

  val configContext: ConfigContext

  trait ConfigContext {
    implicit val config: Config
  }
}

trait DefaultConfigContextComponent extends ConfigContextComponent {
  self: LoggerContextComponent =>

  override lazy val configContext: ConfigContext = new ConfigContext

  class ConfigContext extends super.ConfigContext {
    import loggerContext._

    private var environment: String = "dev"
    try {
      environment = sys.env("env")
    } catch {
      case e: Exception =>
        logger.error("Exception caught: " + e)
    }
    
    implicit val config: Config = ConfigFactory.load(s"application-$environment.conf")
  }
}
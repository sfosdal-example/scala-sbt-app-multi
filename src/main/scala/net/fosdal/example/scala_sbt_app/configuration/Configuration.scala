package net.fosdal.example.scala_sbt_app.configuration

import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import com.typesafe.scalalogging.LazyLogging
import net.fosdal.example.scala_sbt_app.BuildInfo
import pureconfig.syntax._

import scala.concurrent.duration.FiniteDuration

object Configuration extends LazyLogging {

  case class Config(greeting: String, name: String, interval: FiniteDuration, monitoring: MonitoringConfig) {
    def validate(): Unit = {
      logger.info(s"Configuration: $config")
      Validator(config)
    }
  }

  case class MonitoringConfig(console: ConsoleConfig, log: LogConfig, jmx: JmxConfig, datadog: DatadogConfig)

  case class ConsoleConfig(enabled: Boolean, interval: FiniteDuration)

  case class LogConfig(enabled: Boolean, interval: FiniteDuration, logger: String)

  case class JmxConfig(enabled: Boolean, domain: String)

  case class DatadogConfig(enabled: Boolean, interval: FiniteDuration, prefix: String, host: String, tags: Seq[String])

  private[this] lazy val root = ConfigFactory.load()
  lazy val config             = root.getConfig(BuildInfo.configBase).toOrThrow[Config]
  lazy val rendering          = root.root.render(ConfigRenderOptions.concise.setFormatted(true))

  def rendering(path: String): String = {
    ConfigFactory
      .load()
      .getConfig(path)
      .root
      .render(ConfigRenderOptions.concise.setFormatted(true))
  }

}

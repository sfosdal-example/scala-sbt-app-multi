package net.fosdal.example.scala_sbt_app_multi.core

import net.fosdal.example.scala_sbt_app_multi.core.MetricsConfig._

import scala.concurrent.duration.FiniteDuration

object MetricsConfig {

  abstract class BaseMetricsConfig {
    def enabled: Boolean
  }

  abstract class IntervalMetricsConfig extends BaseMetricsConfig {
    def interval: FiniteDuration
  }

  case class JmxMetricsConfig(enabled: Boolean, domain: String) extends BaseMetricsConfig

  case class ConsoleMetricsConfig(enabled: Boolean, interval: FiniteDuration) extends IntervalMetricsConfig

  case class LogMetricsConfig(enabled: Boolean, interval: FiniteDuration, logger: String) extends IntervalMetricsConfig

  case class DatadogMetricsConfig(enabled: Boolean,
                                  interval: FiniteDuration,
                                  prefix: String,
                                  statsdHost: String,
                                  statsdPort: Int,
                                  tags: Seq[String])
      extends IntervalMetricsConfig

}

case class MetricsConfig(jmx: JmxMetricsConfig,
                         console: ConsoleMetricsConfig,
                         log: LogMetricsConfig,
                         datadog: DatadogMetricsConfig)

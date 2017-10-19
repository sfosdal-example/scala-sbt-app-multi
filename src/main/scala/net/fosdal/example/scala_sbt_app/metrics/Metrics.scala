package net.fosdal.example.scala_sbt_app.metrics

import java.lang.management.ManagementFactory

import com.codahale.metrics._
import com.codahale.metrics.jvm._
import net.fosdal.example.scala_sbt_app.configuration.Configuration.MonitoringConfig
import org.coursera.metrics.datadog.DatadogReporter
import org.coursera.metrics.datadog.transport.UdpTransport
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.concurrent.duration._

object Metrics extends Metrics {
  val greets        = registerMeter("greets")
  val greetDuration = registerTimer("greet_duration")
}

trait Metrics {

  private[this] val metricsRegistry = SharedMetricRegistries.setDefault("shared-metrics-registry")

  def registerMeter(name: String): Meter = metricsRegistry.meter(name)

  def registerTimer(name: String): Timer = metricsRegistry.timer(name)

  def registerHistogram(name: String): Histogram = metricsRegistry.histogram(name)

  def registerGauge[A](name: String, value: => A): Gauge[A] = {
    metricsRegistry.register(name, new Gauge[A] {
      override def getValue: A = value
    })
  }

  def timer[A](t: Timer)(block: => A): A = {
    val context = t.time
    try {
      block
    } finally {
      val _ = context.stop
    }
  }

  def apply(config: MonitoringConfig): Unit = {

    // register jvm metrics
    registerAll("jvm.garbage_collector", new GarbageCollectorMetricSet)
    registerAll("jvm.buffer_pool", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer))
    registerAll("jvm.memory_usage", new MemoryUsageGaugeSet())
    registerAll("jvm.thread_states", new ThreadStatesGaugeSet())
    metricsRegistry.register("jvm.file_descriptor", new FileDescriptorRatioGauge())

    // start reporters
    if (config.jmx.enabled) {
      JmxReporter
        .forRegistry(metricsRegistry)
        .inDomain(config.jmx.domain)
        .build
        .start()
    }
    if (config.console.enabled) {
      ConsoleReporter
        .forRegistry(metricsRegistry)
        .build
        .start(config.console.interval.toNanos, NANOSECONDS)
    }
    if (config.log.enabled) {
      Slf4jReporter
        .forRegistry(metricsRegistry)
        .outputTo(LoggerFactory.getLogger(config.log.logger))
        .build
        .start(config.log.interval.toNanos, NANOSECONDS)
    }
    if (config.datadog.enabled) {
      DatadogReporter
        .forRegistry(metricsRegistry)
        .withHost(config.datadog.host)
        .withPrefix(config.datadog.prefix)
        .withTags(config.datadog.tags.asJava)
        .withTransport(new UdpTransport.Builder().build())
        .build()
        .start(config.datadog.interval.toNanos, NANOSECONDS)
    }
  }

  private[this] def registerAll(prefix: String, metrics: MetricSet): Unit = {
    metrics.getMetrics.asScala.foreach {
      case (name, metricSet: MetricSet) =>
        registerAll(s"$prefix.$name", metricSet)
      case (name, metric) =>
        metricsRegistry.register(s"$prefix.$name", metric)
    }
  }

}

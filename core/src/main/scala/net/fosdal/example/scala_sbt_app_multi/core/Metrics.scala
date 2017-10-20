package net.fosdal.example.scala_sbt_app_multi.core

import java.util.concurrent.TimeUnit.NANOSECONDS

import com.codahale.metrics._
import com.typesafe.scalalogging.LazyLogging
import org.coursera.metrics.datadog.DatadogReporter
import org.coursera.metrics.datadog.transport.UdpTransport
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

trait Metrics extends LazyLogging {

  private[this] val registry  = SharedMetricRegistries.setDefault("shared-metrics-registry")
  private[this] var reporters = Seq.empty[ScheduledReporter]

  def registerCounter(n: String): Counter = {
    registry
      .getCounters()
      .asScala
      .getOrElse(n, registry.register(n, new Counter()))
  }

  def registerMeter(n: String): Meter = {
    registry.getMeters().asScala.getOrElse(n, registry.register(n, new Meter()))
  }

  def registerHistogram(n: String): Histogram = registerHistogram(n, new ExponentiallyDecayingReservoir())

  def registerHistogram(n: String, r: Reservoir): Histogram = {
    registry
      .getHistograms()
      .asScala
      .getOrElse(n, registry.register(n, new Histogram(r)))
  }

  def registerTimer(n: String): Timer = registerTimer(n, new ExponentiallyDecayingReservoir())

  def registerTimer(n: String, r: Reservoir): Timer = {
    registry
      .getTimers()
      .asScala
      .getOrElse(n, registry.register(n, new Timer(r)))
  }

  def registerGauge[A](n: String, a: => A): Gauge[A] = {
    registry.register(n, new Gauge[A] {
      override def getValue: A = a
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

  def apply(config: MetricsConfig): Unit = {
    import config._
    // start reporters
    if (jmx.enabled) {
      JmxReporter
        .forRegistry(registry)
        .inDomain(jmx.domain)
        .build
        .start()
    }
    if (console.enabled) {
      val reporter = ConsoleReporter
        .forRegistry(registry)
        .build
      reporter.start(console.interval.toNanos, NANOSECONDS)
      reporters = reporters :+ reporter
    }
    if (log.enabled) {
      val reporter = Slf4jReporter
        .forRegistry(registry)
        .outputTo(LoggerFactory.getLogger(log.logger))
        .build
      reporter.start(log.interval.toNanos, NANOSECONDS)
      reporters = reporters :+ reporter
    }
    if (datadog.enabled) {
      val transport = new UdpTransport.Builder()
        .withStatsdHost(datadog.statsdHost)
        .withPort(datadog.statsdPort)
        .build()
      val reporter = DatadogReporter
        .forRegistry(registry)
        .withPrefix(datadog.prefix)
        .withTags(datadog.tags.asJava)
        .withTransport(transport)
        .build()
      reporter.start(datadog.interval.toNanos, NANOSECONDS)
      reporters = reporters :+ reporter
    }
  }

  def report(): Unit = reporters.foreach(r => r.report())

  def register(prefix: String, metrics: MetricSet): Unit = {
    metrics.getMetrics.asScala.foreach {
      case (name, metricSet: MetricSet) =>
        register(s"$prefix.$name", metricSet)
      case (name, metric) =>
        registry.register(s"$prefix.$name", metric)
    }
  }

}

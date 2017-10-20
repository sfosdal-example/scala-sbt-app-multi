import sbt._

object Dependencies {

  // Logging Dependencies
  private[this] val Log4jVersion        = "2.9.1"
  private[this] val ScalaLoggingVersion = "3.7.2"
  private[this] val Slf4jApiVersion     = "1.7.25"

  val Log4jApi       = "org.apache.logging.log4j"   % "log4j-api"        % Log4jVersion
  val Log4jCore      = "org.apache.logging.log4j"   % "log4j-core"       % Log4jVersion
  val Log4jSlf4jImpl = "org.apache.logging.log4j"   % "log4j-slf4j-impl" % Log4jVersion
  val ScalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"   % ScalaLoggingVersion
  val Slf4jApi       = "org.slf4j"                  % "slf4j-api"        % Slf4jApiVersion

  // Test Dependencies
  private[this] val ScalaCheckVersion = "1.13.5"
  private[this] val ScalaMockVersion  = "3.6.0"
  private[this] val ScalaTestVersion  = "3.0.4"

  val ScalaCheck = "org.scalacheck" %% "scalacheck"                  % ScalaCheckVersion % Test
  val ScalaMock  = "org.scalamock"  %% "scalamock-scalatest-support" % ScalaMockVersion  % Test
  val ScalaTest  = "org.scalatest"  %% "scalatest"                   % ScalaTestVersion  % Test

  // General Dependencies
  private[this] val MetricsDatadogVersion = "1.1.13"
  private[this] val MetricsVersion        = "3.2.5"
  private[this] val OsloVersion           = "0.3.1"
  private[this] val PureconfigVersion     = "0.8.0"
  private[this] val TypesafeConfigVersion = "1.3.2"

  val MetricsCore         = "io.dropwizard.metrics" % "metrics-core"         % MetricsVersion
  val MetricsDatadog      = "org.coursera"          % "metrics-datadog"      % MetricsDatadogVersion
  val MetricsHealthChecks = "io.dropwizard.metrics" % "metrics-healthchecks" % MetricsVersion
  val MetricsJvm          = "io.dropwizard.metrics" % "metrics-jvm"          % MetricsVersion
  val MetricsLog4j2       = "io.dropwizard.metrics" % "metrics-log4j2"       % MetricsVersion
  val Oslo                = "net.fosdal"            %% "oslo"                % OsloVersion
  val Pureconfig          = "com.github.pureconfig" %% "pureconfig"          % PureconfigVersion
  val TypesafeConfig      = "com.typesafe"          % "config"               % TypesafeConfigVersion

}

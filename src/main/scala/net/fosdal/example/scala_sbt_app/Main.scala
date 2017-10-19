package net.fosdal.example.scala_sbt_app

import com.typesafe.scalalogging.LazyLogging
import net.fosdal.example.scala_sbt_app.configuration.Configuration._
import net.fosdal.example.scala_sbt_app.metrics.Metrics
import net.fosdal.example.scala_sbt_app.metrics.Metrics._

object Main extends App with LazyLogging {

  logger.info(s"BuildInfo: ${BuildInfo.toJson}")
  logger.info(s"Configuration File:\n${rendering(BuildInfo.configBase)}")
  config.validate()

  Metrics(config.monitoring)

  val greeter = Greeter(config.greeting)
  val msg     = greeter(config.name)

  while (true) {
    timer(greetDuration) {
      greets.mark()
      logger.info(msg)
      Thread.sleep(config.interval.toMillis)
    }
  }

}

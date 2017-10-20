package net.fosdal.example.scala_sbt_app_multi

import com.typesafe.scalalogging.LazyLogging
import net.fosdal.example.scala_sbt_app_multi.Configuration.config
import net.fosdal.example.scala_sbt_app_multi.Metrics._
import net.fosdal.example.scala_sbt_app_multi.core.Greeter
import net.fosdal.oslo._

object Main extends App with LazyLogging {

  logger.info(s"BuildInfo: ${BuildInfo.toJson}")
  logger.info(s"Configuration File:\n${Configuration.rendering(BuildInfo.configBase)}")

  Configuration.validate(config)

  Metrics(config.monitoring)

  val greeter = Greeter(config.greeting)

  while (true) {
    timer(greetDuration) {
      greets.mark()
      logger.info(greeter(config.name))
      sleep(config.greetInterval)
    }
  }

}

package net.fosdal.example.scala_sbt_app_multi

import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import com.typesafe.scalalogging.LazyLogging
import net.fosdal.example.scala_sbt_app_multi.core.MetricsConfig
import pureconfig.syntax._

import scala.concurrent.duration.FiniteDuration

object Configuration extends LazyLogging {

  case class Config(greeting: String, name: String, greetInterval: FiniteDuration, monitoring: MetricsConfig) {

    val hardValidations: Config => Seq[String] = (_: Config) => Nil

    val softValidations: Map[String => Unit, Config => Seq[String]] = Map(
      (logger.error(_: String)) -> ((_: Config) => Nil),
      (logger.warn(_: String))  -> ((_: Config) => Nil),
      (logger.info(_: String))  -> ((_: Config) => Nil),
      (logger.debug(_: String)) -> ((_: Config) => Nil),
      (logger.trace(_: String)) -> ((_: Config) => Nil)
    )

  }

  private[this] lazy val root = ConfigFactory.load()
  lazy val config             = root.getConfig(BuildInfo.configBase).toOrThrow[Config]
  lazy val rendering          = root.root.render(ConfigRenderOptions.concise.setFormatted(true))

  def rendering(path: String): String = {
    root
      .getConfig(path)
      .root
      .render(ConfigRenderOptions.concise.setFormatted(true))
  }

  def validate(c: Config): Unit = {
    logger.info(s"Configuration: $c")
    c.softValidations.foreach({ case (lgr, msgs) => msgs(c).foreach(lgr) })
    c.hardValidations(c).mkString("; ") match {
      case msg: String if msg.nonEmpty =>
        throw new Exception(msg)
      case _ =>
    }
  }

}

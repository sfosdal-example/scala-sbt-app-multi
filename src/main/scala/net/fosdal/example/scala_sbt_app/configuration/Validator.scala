package net.fosdal.example.scala_sbt_app.configuration

import com.typesafe.scalalogging.LazyLogging
import net.fosdal.example.scala_sbt_app.configuration.Configuration.Config

object Validator extends LazyLogging {

  private[this] val failures: Config => Seq[String] = (_: Config) => Nil

  private[this] val loggers: Map[String => Unit, Config => Seq[String]] = Map(
    (logger.error(_: String)) -> ((_: Config) => Nil),
    (logger.warn(_: String))  -> ((_: Config) => Nil),
    (logger.info(_: String))  -> ((_: Config) => Nil),
    (logger.debug(_: String)) -> ((_: Config) => Nil),
    (logger.trace(_: String)) -> ((_: Config) => Nil)
  )

  def apply(c: Config): Unit = {
    loggers.foreach({ case (lgr, msgs) => msgs(c).foreach(lgr) })

    val allFailures = failures(c).mkString("; ")
    if (allFailures.nonEmpty) {
      throw new Exception(allFailures)
    }

  }

}

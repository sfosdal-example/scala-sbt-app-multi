package net.fosdal.example.scala_sbt_app_multi

object Metrics extends core.Metrics {

  val greets        = registerMeter("greets")
  val greetDuration = registerTimer("greet_duration")

}

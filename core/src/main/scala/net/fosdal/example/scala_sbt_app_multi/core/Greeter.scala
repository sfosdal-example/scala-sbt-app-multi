package net.fosdal.example.scala_sbt_app_multi.core

case class Greeter(greeting: String) {

  def apply(name: String): String = s"$greeting $name"

}

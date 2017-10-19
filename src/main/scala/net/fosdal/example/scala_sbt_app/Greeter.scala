package net.fosdal.example.scala_sbt_app

case class Greeter(greeting: String) {

  def apply(name: String): String = s"$greeting $name"

}

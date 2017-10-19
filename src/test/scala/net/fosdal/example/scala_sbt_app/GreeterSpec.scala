package net.fosdal.example.scala_sbt_app

import org.scalatest.{Matchers, WordSpec}

class GreeterSpec extends WordSpec with Matchers {

  "Greeter" when {
    "given a greeting and a name" must {
      "greet that name" in new Fixture {
        val greeter = Greeter(greeting)
        val msg     = greeter(name)
        msg shouldBe "Hello World"
      }
    }
  }

  trait Fixture {
    val greeting = "Hello"
    val name     = "World"
  }

}

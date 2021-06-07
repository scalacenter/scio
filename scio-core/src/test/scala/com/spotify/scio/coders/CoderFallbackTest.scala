package com.spotify.scio.coders

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CoderFallbackTest extends AnyFlatSpec with Matchers {

  "fallback" should "not compile if an implicit is in scope" in {
    case class SimpleCaseClass(n: Int)
    given Coder[SimpleCaseClass] = Coder.apply[SimpleCaseClass]
    "Coder.fallback[SimpleCaseClass]" shouldNot compile
  }

  it should "compile if an implicit is not in scope" in {
    "Coder[Int | String]" shouldNot compile
  }

  it should "trigger the error message when no implicit is in scope" in {
    /*
     Fallback only compiles when no Coder is in scope, thanks to NotGiven.
     Therefore it is not necessary to capture and parse the warning compilation
     message, if this line compiles, then the message will be printed
    */
    "Coder.fallback[Int | String]" should compile
  }

}
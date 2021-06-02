package com.spotify.scio.coders

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CoderFallbackTest extends AnyFlatSpec with Matchers {

  "fallback" should "not compile if an implicit is in scope" in {
    case class SimpleCaseClass(n: Int)
    given Coder[SimpleCaseClass] = Coder.apply[SimpleCaseClass]
    "Coder.fallback[SimpleCaseClass]" shouldNot compile
  }

  "fallback" should "compile if an implicit is not in scope" in {
    println(Coder[Int | String])
    true
  }

}
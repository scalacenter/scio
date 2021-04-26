package com.spotify.scio

import com.spotify.scio.schemas.To

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

case class JavaListInt(l: java.util.List[java.lang.Integer])
case class JavaListString(l: java.util.List[java.lang.String])
case class ListInt(l: List[Int])
case class JavaSource(b: java.lang.Boolean)
case class Source(b: Boolean)
case class Dest(b: Boolean)
case class Mistake(b: Int)
case class Mistake2(c: Boolean)

case class Sources(name: String, links: List[Array[Byte]])
case class Destinations(name: String, links: List[Array[Byte]])
case class DestinationsWrong(name: String, links: List[Array[Int]])

class ToSafeTest extends AnyFlatSpec with Matchers {
  "To.safe" should "generate a conversion on compatible flat case class schemas" in {
    To.safe[Source, Dest]
  }

  it should "generate a conversion between java.lang.Boolean and Boolean" in {
    To.safe[JavaSource, Source]
    To.safe[Source, JavaSource]
  }
  
  it should "generate a conversion between java.util.List[java.lang.Integer] and List[Int]" in {
    To.safe[JavaListInt, ListInt]
    To.safe[ListInt, JavaListInt]
  }

  it should "fail on incompatible Java types" in {
    "To.safe[JavaListString, JavaListInt]" shouldNot compile
    "To.safe[JavaListString, ListInt]" shouldNot compile
  }

  it should "fail on incompatible flat case class schemas" in {
    "To.safe[Source, Mistake2]" shouldNot compile
    "To.safe[Source, Mistake]" shouldNot compile
  }

  it should "generate a conversion on compatible nested case class schemas" in {
    To.safe[Sources, Destinations]
  }

  it should "fail on incompatible nested case class schemas" in {
    "To.safe[Sources, DestinationsWrong]" shouldNot compile
  }

  it should "work with java beans" in {
    "To.safe[JavaBeanA, JavaBeanB]" shouldNot compile
    "To.safe[JavaBeanB, JavaBeanA]" shouldNot compile

    "To.safe[JavaBeanB, JavaBeanC]" shouldNot compile
    "To.safe[JavaBeanC, JavaBeanB]" shouldNot compile

    To.safe[JavaBeanA, JavaBeanC](com.spotify.scio.schemas.Schema.javaBeanSchema[JavaBeanA], com.spotify.scio.schemas.Schema.javaBeanSchema[JavaBeanC])
    To.safe[JavaBeanC, JavaBeanA]
  }
}

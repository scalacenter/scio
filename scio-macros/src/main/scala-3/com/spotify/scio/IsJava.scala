/*
 * Copyright 2019 Spotify AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spotify.scio

import scala.compiletime._
import scala.deriving._
import scala.quoted._

/** Proof that a type is implemented in Java */
sealed trait IsJavaBean[T]

object IsJavaBean {

  private[scio] def checkGetterAndSetters[T: scala.quoted.Type](using Quotes): Unit = {
    import quotes.reflect._
    val methods: List[Symbol] = TypeRepr.of[T].typeSymbol.declaredMethods

    val getters: List[(String, Symbol)] =
      methods.collect {
        case s if s.name.toString.startsWith("get") && s.isDefDef =>
          (s.name.toString.drop(3), s)
      }

    val setters: Map[String, Symbol] =
      methods.collect {
        case s if s.name.toString.startsWith("set") && s.isDefDef =>
          (s.name.toString.drop(3), s)
      }.toMap

    if (getters.isEmpty) then {
      val mess =
        s"""Class ${TypeRepr.of[T].typeSymbol.name} has not getter"""
      report.throwError(mess)
    }

    getters.foreach { case (name, getter) =>
      val setter: Symbol =
        setters // Map[String, DefDef]
          .get(name)
          .getOrElse {
            val mess =
              s"""JavaBean contained a getter for field $name""" +
                """ but did not contain a matching setter."""
            report.throwError(mess)
          }

      val getterType: TypeRepr = TypeRepr.of[T].memberType(getter)
      val setterType: TypeRepr = TypeRepr.of[T].memberType(setter)
      (getterType, setterType) match {
        // MethodType(paramNames, paramTypes, returnType)
        case (MethodType(_, Nil, getReturnType), MethodType(_, setReturnType :: Nil, _)) =>
          if getReturnType != setReturnType then {
            val mess =
              s"""JavaBean contained setter for field $name that had a mismatching type.
                    |  found:    $setReturnType
                    |  expected: $getReturnType""".stripMargin
            report.throwError(mess)
          }
      }
    }
  }


  private def isJavaBeanImpl[T](using Quotes, Type[T]): Expr[IsJavaBean[T]] = {
    import quotes.reflect._
    if TypeRepr.of[T].typeSymbol.flags.is(Flags.JavaDefined) && checkGetterAndSetters[T] then
      '{new IsJavaBean[T]{}}
    esle report.error(s"${summon[Type[T]].show} is not a Java Bean")
  }

  inline given isJavaBean[T]: IsJavaBean[T] = {
    ${ isJavaBeanImpl[T] }
  }

  def apply[T](using i: IsJavaBean[T]): IsJavaBean[T] = i
}

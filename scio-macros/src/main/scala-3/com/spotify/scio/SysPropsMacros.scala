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

import scala.annotation.{compileTimeOnly, StaticAnnotation}
import com.spotify.scio.SysProps

import scala.quoted.*

object SysPropsMacros {
  inline def propertiesIn[T <: SysProps](inline self: T): List[SysProp] = 
    ${ propertiesInImpl('self) }

  def propertiesInImpl[T <: SysProps : Type](self: Expr[T])(using quotes: Quotes): Expr[List[SysProp]] ={
    import quotes.reflect._
    val typeOfSubclass = TypeTree.of[T]
    val sym = typeOfSubclass.symbol
    val typesOfVals: List[TypeRepr] = sym.memberFields.map(_.tree).collect { 
      case vd: ValDef => vd.tpt.tpe
    }
    val props: List[Symbol] = sym.memberFields.filter(_.tree match {
      case vd: ValDef => vd.tpt.tpe.typeSymbol == TypeRepr.of[SysProp].typeSymbol
      case _ => false
    })
    val x = Ident(props.head)
      
    println(props)
    '{Nil}
  }
}


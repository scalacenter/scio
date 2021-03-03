/*
 * Copyright 2020 Spotify AB.
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

package com.spotify.scio.macros

import scala.compiletime._
import scala.deriving._

object DerivationUtils {
  inline given mirrorFields[Fields <: Tuple] as List[String] =
    inline erasedValue[Fields] match {
      case _: (field *: fields) => constValue[field].toString :: mirrorFields[fields]
      case _ => Nil
    }

  inline given summonAllF[F[_], T <: Tuple] as Widen[T] = {
    val res =
      inline erasedValue[T] match {
        case _: EmptyTuple => EmptyTuple
        case _: (t *: ts) => summonInline[F[t]] *: summonAllF[F, ts]
      }
    res.asInstanceOf[Widen[T]]
  }
}
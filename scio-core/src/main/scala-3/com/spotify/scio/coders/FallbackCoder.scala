package com.spotify.scio.coders

import scala.reflect.ClassTag
import com.spotify.scio.coders.macros.FallbackCoderMacros

trait FallbackCoder {
  inline def fallback[T](using scala.util.NotGiven[Coder[T]]): Coder[T] =
    ${ FallbackCoderMacros.issueFallbackWarning[T] }
}

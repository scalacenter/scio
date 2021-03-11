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

import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.reflect.ClassPath

object SysProps {
  import scala.jdk.CollectionConverters._
  import scala.reflect.runtime.universe

  def properties: Iterable[SysProps] = {
    val classLoader = Thread.currentThread().getContextClassLoader
    val runtimeMirror = universe.runtimeMirror(classLoader)
    ClassPath
      .from(classLoader)
      .getAllClasses
      .asScala
      .filter(_.getName.endsWith("SysProps"))
      .flatMap { clsInfo =>
        try {
          val cls = clsInfo.load()
          cls.getMethod("properties")
          val module = runtimeMirror.staticModule(cls.getName)
          val obj = runtimeMirror.reflectModule(module)
          Some(obj.instance.asInstanceOf[SysProps])
        } catch {
          case _: Throwable => None
        }
      }
  }
}

@registerSysProps
object CoreSysProps {
  val Project: SysProp = SysProp("project", "")
  val Home: SysProp = SysProp("java.home", "java home directory")
  val TmpDir: SysProp = SysProp("java.io.tmpdir", "java temporary directory")
  val User: SysProp = SysProp("user.name", "system username")
  val UserDir: SysProp = SysProp("user.dir", "user dir")
}

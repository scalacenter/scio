package com.spotify.scio

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SysPropsMacrosTest extends AnyFlatSpec with Matchers {
  @registerSysProps
  object TestSysProps1 extends SysProps {
    val TmpDir: SysProp = SysProp("java.io.tmpdir", "java temporary directory")
  }

  "TestSysProps1" should "have one SysProp" in {
    assert(TestSysProps1.properties.size == 1)
  }
}

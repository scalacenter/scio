package com.spotify.scio

import org.scalatest.funsuite.AnyFunSuite

class SysPropsMacrosTest extends AnyFunSuite {
  object TestSysProps1 extends SysProps {
    val TmpDir: SysProp = SysProp("java.io.tmpdir", "java temporary directory")
  }

  test("TestSysProps1 should have one SysProp") {
    assert(TestSysProps1.properties.size == 1)
  }
}

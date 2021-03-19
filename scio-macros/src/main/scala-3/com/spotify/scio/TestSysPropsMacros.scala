package com.spotify.scio

object Maxime extends SysProps {
  val TmpDir: SysProp = SysProp("java.io.tmpdir", "java temporary directory")

  override def properties: List[SysProp] = SysPropsMacros.propertiesIn(this)
}

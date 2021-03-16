package com.spotify.scio

trait SysProps extends AbstractSysProps {
  override def properties: List[SysProp] = SysPropsMacros.propertiesIn(this)
}

/**
 * This annotation does nothing on Scala 3, it is only used for cross-compatibility
 * with Scala 2.
 */
final class registerSysProps extends StaticAnnotation

package com.spotify.scio

final case class SysProp(flag: String, description: String) {
  def value(default: => String): String = sys.props.getOrElse(flag, default)

  def value: String = sys.props(flag)

  def valueOption: Option[String] = sys.props.get(flag)

  def value_=(str: String): Unit =
    sys.props(flag) = str

  def show: String =
    s"-D$flag=<String>\n\t$description"
}

/**
 * Common interface for Scala 2 and Scala 3 SysProps implementations. To write
 * a cross-compatible object extending SysProps:
 *
 * {{{
 * @registerSysProps
 * object MySysProps extends SysProps {
 *   val MyProp = SysProp("...", "...")
 * }
 * }}}
 *
 * @see com.spotify.scio.SysProps
 */
private[scio] trait AbstractSysProps {
  def properties: List[SysProp]

  def show: String = {
    val props = properties.map(p => s"  ${p.show}").mkString("\n")
    val name = this.getClass.getName.replace("$", "")
    s"$name:\n$props\n"
  }
}

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

trait SysProps {
  def properties: List[SysProp]

  def show: String = {
    val props = properties.map(p => s"  ${p.show}").mkString("\n")
    val name = this.getClass.getName.replace("$", "")
    s"$name:\n$props\n"
  }
}

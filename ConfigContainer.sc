class ConfigContainer(port: Int, root: String) {
  def getPort: Int = port
  def getRoot: String = root
}

case class ServerConfig(port: Int, root: String) extends ConfigContainer(port, root)
case object NoConfig extends ConfigContainer(8080, ".")

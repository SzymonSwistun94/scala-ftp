import java.io.{File, PrintWriter}

import $file.Imports

import $file.ConfigContainer
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}

object ConfigHandler {

  private val configFile: File = new File("settings.conf")
  private val config: Config = if (configFile.exists())
    ConfigFactory.parseFile(configFile)
  else {
    val tmp = ConfigFactory.empty.withValue("port", ConfigValueFactory.fromAnyRef(8080)).withValue("root", ConfigValueFactory.fromAnyRef("."))
    val string = tmp.root().render()
    val printer = new PrintWriter(configFile)
    printer.print(string)
    printer.flush()
    printer.close()
    tmp
  }

  def getConfig: ConfigContainer.ConfigContainer =
    ConfigContainer.ServerConfig(config.getInt("port"), config.getString("root"))
}

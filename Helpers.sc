import java.io.File

object Helpers {

  def getDirectoryListing(f: File, root: String = ""): String = {
    val (files, dirs) = if (f.list().isEmpty) ("", "")
    else {
      (if (!f.listFiles().isEmpty) {
        val l = f.listFiles().filter(_.isFile).map(f => {
          val link = fileLink(f, root)
          link
        })
        s"Files: \n${l.mkString("\n")}"
      }
      else "",
        if (f.listFiles().exists(_.isDirectory)) {
          val l = f.listFiles().filter(_.isDirectory).map(fileLink(_, root))
          s"Directories: \n${l.mkString("\n")}"
        }
        else ""
      )
    }
    toHtmlString(dirs + "\n\n" + files)
  }

  def getDirectoryListingJson(dir: File, root: String = ""): String = {
    val (files, dirs) = if (dir.list().isEmpty) ("", "")
    else {
      (if (!dir.listFiles().isEmpty) {

        val l = dir.listFiles().filter(_.isFile).map(f => {
          val link = "\"" + rawFileLink(f, root) + "\""
          val name = "\"" + f.getName + "\""
          "{\n" +
            "\"name\": " + name + ",\n" +
            "\"link\": " + link + "\n" +
            "}\n"
        })

        l.mkString(",\n")
      }
      else "",
        if (dir.list.exists(p => new File(p).isDirectory)) {

          val l = dir.list().map(new File(_)).filter(_.isDirectory).map(f => {
            val link = "\"" + rawFileLink(f, root) + "\""
            val name = "\"" + f.getName + "\""
            "{\n" +
              "\"name\": " + name + ",\n" +
              "\"link\": " + link + "\n" +
              "}\n"
          })
          l.mkString(",\n")

        }
        else ""
      )
    }
    "{\n" +
      "\"directories\": [\n" +
      s"$dirs\n" +
      "],\n" +
      "\"files\": [\n" +
      s"$files\n" +
      "]\n" +
      "}"
  }

  def toSimpleHtml(title: String, str: String): String = {
    "<!DOCTYPE html>" +
      "<html>" +
      "<head>" +
      "<meta charset=\"UTF-8\">" +
      s"<title>$title</title>" +
      "</head>" +
      "<body>" +
      str +
      "</body>" +
      "</html>"
  }

  def fileLink(f: File, root: String): String = {
    val path = f.getAbsolutePath.stripPrefix(root).stripPrefix("/").stripPrefix("\\")
    s"<a href=$path>${f.getName}</a>"
  }

  def rawFileLink(f: File, root: String): String = {
    f.getAbsolutePath.stripPrefix(root).stripPrefix("/").stripPrefix("\\")
  }

  def toHtmlString(s: String): String = s.replaceAll("\n", "<br />")

  def changeToAbsolutePath(relPath: String): String = {
    if (relPath.startsWith("~")) {
      System.getProperty("user.home").stripSuffix("/").stripSuffix("\\") + relPath.stripPrefix("~")
    }
    else if (relPath.startsWith(".")) {
      System.getProperty("user.dir").stripSuffix("/").stripSuffix("\\") + relPath.stripPrefix(".")
    }
    else {
      relPath
    }
  }

}

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{RequestContext, Route}
import akka.stream.ActorMaterializer

import $file.Helpers

import scala.concurrent.ExecutionContextExecutor

class RoutingEngine(r: String)(implicit val system: ActorSystem, implicit val materializer: ActorMaterializer, implicit val executionContext: ExecutionContextExecutor) {

  val types = Map(
    "json" -> 0,
    "http" -> 1
  )

  val root: String = Helpers.Helpers.changeToAbsolutePath(r)

  def getFilesystemRoute: Route = {
    val route: Route = (request: RequestContext) => {
      val uri = request.request.getUri().toRelative
      val (stringURL, contentType) = {
        val tmpURL = uri.toString.stripPrefix("/")
        if (tmpURL.startsWith("json")) {
          (tmpURL.stripPrefix("json"), "json")
        }
        else {
          ("/" + tmpURL, "html")
        }
      }

      val exactPath = root.stripSuffix("/").stripSuffix("\\") + stringURL
      val f = new File(exactPath)

      if (f.exists()) {
        if (f.isDirectory) {

          contentType match {
            case "json" =>
              println("json")
              request.complete(HttpEntity(ContentTypes.`application/json`, Helpers.Helpers.getDirectoryListingJson(f, root)))
            case "html" =>
              println("http")
              request.complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, Helpers.Helpers.getDirectoryListing(f, root)))
          }
        }
        else {
          getFromFile(f).apply(request)
        }
      }
      else {
        request.complete(StatusCodes.NotFound)
      }
    }

    route
  }

}

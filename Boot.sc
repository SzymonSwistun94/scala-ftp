import $file.Imports
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import $file.ConfigHandler
import $file.ConfigContainer
import $file.RoutingEngine
import $file.Helpers

import scala.concurrent.{ExecutionContextExecutor, Future}

implicit val system: ActorSystem = ActorSystem("my-system")
implicit val materializer: ActorMaterializer = ActorMaterializer()

implicit val executionContext: ExecutionContextExecutor = system.dispatcher

val config: ConfigContainer.ConfigContainer = ConfigHandler.ConfigHandler.getConfig

val routingEngine = new RoutingEngine.RoutingEngine(config.getRoot)

val route: Route = {
  routingEngine.getFilesystemRoute
}

val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", config.getPort)

println(s"Server online at http://localhost:8080/")

var running = true

println("Press enter to terminate")
scala.io.StdIn.readLine()

Http().shutdownAllConnectionPools() andThen { case _ => system.terminate() }
println("Shutdown complete")
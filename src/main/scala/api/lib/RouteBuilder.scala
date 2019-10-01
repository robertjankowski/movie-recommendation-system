package api.lib

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object RouteBuilder {

  def create(host: String,
             port: Int,
             routes: List[Routing])
            (implicit actorSystem: ActorSystem,
             materializer: ActorMaterializer): Unit = {
    val allRoutes = routes
      .map(_.route)
      .reduce(_ ~ _)
    Http().bindAndHandle(allRoutes, host, port)
  }

}

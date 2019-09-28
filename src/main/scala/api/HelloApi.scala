package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.lib.Routing

class HelloApi extends Routing {
  override def route: Route = path("app") {
    complete("Hello from app")
  }
}

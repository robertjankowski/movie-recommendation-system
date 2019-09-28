package api.lib

import akka.http.scaladsl.server.Route

trait Routing {
  def route: Route
}

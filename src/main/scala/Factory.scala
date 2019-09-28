import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import api.HelloApi
import api.lib.RouteBuilder

class Factory(configuration: MusicAppConfiguration)
             (implicit actorSystem: ActorSystem,
              materializer: ActorMaterializer) {

  def createApi(): Unit =  {
    RouteBuilder.create(
      configuration.host,
      configuration.port,
      new HelloApi :: Nil
    )
  }
  createApi()
}

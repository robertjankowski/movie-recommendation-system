import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import core.encoders._

object Boot extends App with LazyLogging {
  implicit val system: ActorSystem = ActorSystem("music-recommendation-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val factory = new Factory(new MusicAppConfigurationImpl(config))
  factory.createApi()

  val ss = factory.createSparkSession()
  val sc = ss.sparkContext
  sc.setLogLevel("ERROR")

  val movies = factory.loadMovies(ss)
  val ratings = factory.loadUsersRatings(ss)

  movies.printSchema()
  ratings.printSchema()

  movies.show(5)
  ratings.show(5)
  ss.stop()
}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import api.HelloApi
import api.lib.RouteBuilder
import core.domain.{Movie, User}
import org.apache.spark.sql.{Dataset, Encoder, SparkSession}

class Factory(configuration: MusicAppConfiguration)
             (implicit actorSystem: ActorSystem,
              materializer: ActorMaterializer) {

  def createApi(): Unit = {
    RouteBuilder.create(
      configuration.host,
      configuration.port,
      new HelloApi :: Nil
    )
  }

  def createSparkSession(): SparkSession = SparkSession
    .builder()
    .master("local[*]")
    .appName("Recommendation app")
    .getOrCreate()

  def loadUsersRatings(ss: SparkSession)(implicit encoder: Encoder[User]): Dataset[User] = ss.read
    .format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("../ratings.csv")
    .as("ratings")
    .map { row =>
      User(
        row.getAs[Int]("userId"),
        row.getAs[Int]("movieId"),
        row.getAs[Double]("rating"))
    }

  def loadMovies(ss: SparkSession)(implicit encoder: Encoder[Movie]): Dataset[Movie] = ss.read
    .format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("../movies.csv")
    .as("movies")
    .map { row =>
      Movie(
        row.getAs[Int]("movieId"),
        row.getAs[String]("title"),
        row.getAs[String]("genres").split('|'))
    }
}

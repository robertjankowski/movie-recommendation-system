import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import core.encoders._
import org.apache.spark.sql.functions._

object Boot extends App with LazyLogging {
  implicit val system: ActorSystem = ActorSystem("music-recommendation-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val factory = new Factory(new MusicAppConfigurationImpl(config))
  factory.createApi()

  val ss = factory.createSparkSession()
  val sc = ss.sparkContext
  sc.setLogLevel("ERROR")

  val movies = factory.loadMovies(ss).cache()
  val ratings = factory.loadUsersRatings(ss).cache()

  val df = ratings.join(movies, Seq("movieId")).cache()
  val pivotItemBased = df
    .groupBy(col("title"))
    .pivot(col("userId"), Seq("rating"))
    .agg(mean("rating"))
    .cache()
  pivotItemBased.printSchema()
  pivotItemBased.show(10)

  ss.stop()
}
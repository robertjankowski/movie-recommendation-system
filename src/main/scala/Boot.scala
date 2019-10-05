import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object Boot extends App with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("music-recommendation-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  //val config = ConfigFactory.load()
  //val factory = new Factory(new MusicAppConfigurationImpl(config))

  val ss = SparkSession
    .builder()
    .appName("Music recommendation system")
    .master("local")
    .getOrCreate()
  val sc = ss.sparkContext
  sc.setLogLevel("ERROR")

  val jdbcDF = ss.read
    .format("jdbc")
    .option("url", "jdbc:postgresql://localhost:5432/docker")
    .option("dbtable", "songs")
    .option("useUnicode", "true")
    .option("continueBatchOnError", "true")
    .option("useSSL", "false")
    .option("user", "docker")
    .option("password", "")
    .load()
  println(jdbcDF.printSchema())

}

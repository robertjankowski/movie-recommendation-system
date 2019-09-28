import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object Boot extends App with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("music-recommendation-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ConfigFactory.load()
  new Factory(new MusicAppConfigurationImpl(config))

  def testSpark = {

    val ss = SparkSession
      .builder()
      .appName("Music recommendation system")
      .master("local[4]")
      .getOrCreate()

    val sc = ss.sparkContext
    sc.setLogLevel("ERROR")

    val rddL = sc.parallelize((1 to 1E6.toInt).toList, 4)

    val sumOddNumber = rddL
      .filter(_ % 2 != 0)
      .sum()
    println(s"Sum of odd number ${sumOddNumber.longValue()}")
  }
}

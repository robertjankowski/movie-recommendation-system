import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

object Boot extends App with LazyLogging {

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


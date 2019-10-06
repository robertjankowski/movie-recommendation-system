import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Encoders, SparkSession}

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

  //  val jdbcDF = ss.read
  //    .format("jdbc")
  //    .option("url", "jdbc:postgresql://localhost:5432/docker")
  //    .option("dbtable", "songs")
  //    .option("useUnicode", "true")
  //    .option("continueBatchOnError", "true")
  //    .option("useSSL", "false")
  //    .option("user", "docker")
  //    .option("password", "")
  //    .load()
  //  println(jdbcDF.printSchema())

  val songData = ss.read
    .format("csv")
    .option("header", "true")
    .load("../song_data.csv")
    .as("song_data")

  val songs = ss.read
    .format("csv")
    .option("header", "true")
    .option("delimiter", "\t")
    .load("../songs.txt")
    .as("songs")

  val df = songs.join(songData, Seq("song_id"), "left")

  final case class Rating(userId: Int, songId: Int, listenCount: Int)

  implicit val ratingEncoder = Encoders.product[Rating]
  val dfNew = df.select(
    col("user_id"),
    col("song_id"),
    col("listen_count")
  ).map {
    r =>
      Rating(
        r.getAs[String]("user_id").hashCode,
        r.getAs[String]("song_id").hashCode,
        r.getAs[String]("listen_count").toInt
      )
  }
  val Array(trainData, testData) = dfNew.randomSplit(Array(0.8, 0.2))

  // Build the recommendation model using ALS
  val als = new ALS()
    .setMaxIter(5)
    .setRegParam(0.1)
    .setUserCol("userId")
    .setItemCol("songId")
    .setRatingCol("listenCount")

  val model = als.fit(trainData)

  model.setColdStartStrategy("drop")
  val predictions = model.transform(testData)

  val evaluator = new RegressionEvaluator()
    .setMetricName("rmse")
    .setLabelCol("listenCount")
    .setPredictionCol("prediction")
  val rmse = evaluator.evaluate(predictions)
  println(s"RSME = $rmse")

  val userRecs = model.recommendForAllUsers(10)
  val songRecs = model.recommendForAllItems(10)

  val users = dfNew.select(als.getUserCol).distinct().limit(3)
  val userSubsetRecs = model.recommendForUserSubset(users, 10)

  val songsDf = dfNew.select(als.getItemCol).distinct().limit(3)
  val songsSubSetRecs = model.recommendForItemSubset(songsDf, 10)
  userRecs.show(truncate = false)
  songRecs.show(truncate = false)
  userSubsetRecs.show(truncate = false)
  songsSubSetRecs.show(truncate = false)

  ss.stop()

}

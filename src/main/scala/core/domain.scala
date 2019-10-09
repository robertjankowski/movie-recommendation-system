package core

import core.domain.{Movie, User}
import org.apache.spark.sql.Encoders

object domain {

  final case class User(userId: Int, movieId: Int, rating: Double)

  final case class Movie(movieId: Int, title: String, genres: Array[String])
}

object encoders {
  implicit val userEncoder = Encoders.product[User]
  implicit val movieEncoder = Encoders.product[Movie]
}

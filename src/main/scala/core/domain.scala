package core

import core.domain.{Movie, User}
import org.apache.spark.sql.{Encoder, Encoders}

object domain {

  final case class User(userId: Int, movieId: Int, rating: Double)

  final case class Movie(movieId: Int, title: String, genres: Array[String])
}

object encoders {
  implicit val userEncoder: Encoder[User] = Encoders.product[User]
  implicit val movieEncoder: Encoder[Movie] = Encoders.product[Movie]
}

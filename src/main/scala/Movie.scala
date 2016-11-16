import org.joda.time.Duration

import scala.collection.mutable

/**
  * Created by tobi on 13.11.16.
  */
class Movie private (val name: String, val duration: Duration) {
  override def toString = name
}

object Movie {
  val movies = mutable.Map[String, Movie]()

  def apply(name: String, duration: Duration): Movie = movies.getOrElseUpdate(name, new Movie(name, duration))
  def apply(name: String): Movie = movies(name)

  def allMovies = movies.values
  def allNames = movies.keys
}

import org.joda.time.DateTime
import com.github.nscala_time.time.Imports._

import scala.collection.mutable
import scalax.collection.GraphEdge.DiEdge
import scalax.collection.GraphTraversal._
import scalax.collection.mutable.Graph

/**
  * Created by tobi on 12.11.16.
  */
case class Screening(movie: Movie, time: DateTime, location: Cinema) {
  override def toString = {
    val hoursMinuteFormatter = DateTimeFormat.forPattern("d-HH:mm")
    s"[${movie.name} @ ${hoursMinuteFormatter.print(time)} @ $location]"
  }
}

object ScreeningGraph {
  val graph = Graph[Screening, DiEdge]()

  private val startNode = Screening(Movie("startNode", 0.minutes), DateTime.now, DummyLocation)
  private val endNode = Screening(Movie("endNode", 0.minutes), DateTime.now, DummyLocation)

  def buildGraph(screenings: List[Screening]) = {
    val screeningDays = screenings.map(_.time.getDayOfYear).toSet

    val screeningByCinema = screenings.map(s => s.location -> screenings.filter(_.location == s.location).sortBy(_.time)).toMap

    val screeningByDoY = screenings.map(s => s.time.dayOfYear() -> s).toMap

    for((cinema, filmlist) <- screeningByCinema) {
      val modifiedFilmList = startNode +: filmlist :+ endNode
      val consecutiveFilms = modifiedFilmList.dropRight(1).zip(modifiedFilmList.tail)

      for((first, follower) <- consecutiveFilms) {
        graph += DiEdge(first, follower)
      }
    }

    //todo: add edges for reachable screenings
  }

  def buildPaths(): List[graph.Path] = {
    val outerStartNode = graph.get(startNode)
    val outerEndNode = graph.get(endNode)

    def buildPaths_rec(current: graph.NodeT, currentPath: graph.PathBuilder): List[graph.PathBuilder] = {
      val nextNodes = graph.get(current).diSuccessors

      //println(s"on node: $current, next nodes: $nextNodes")

      if (nextNodes.isEmpty) {
        return List(currentPath)
      }

      val returnList = mutable.MutableList[graph.PathBuilder]()

      val nodeToKeepBuilder = nextNodes.head

      for (nextNode <- nextNodes) {
        val path = if (nextNode == nodeToKeepBuilder) {
          currentPath
        } else {
          val newPath = graph.newPathBuilder(outerStartNode)
          val pathToHere = currentPath.result()
          for (pathElement <- pathToHere) {
            newPath += pathElement
          }

          newPath
        }

        path += nextNode

        //println(s"next node: $nextNode, path: ${path.result}")

        returnList ++= buildPaths_rec(nextNode, path)
      }

      returnList.toList
    }

    buildPaths_rec(outerStartNode, graph.newPathBuilder(outerStartNode)).map(path => path.result())

  }

}

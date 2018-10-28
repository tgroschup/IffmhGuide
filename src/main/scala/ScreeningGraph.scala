import com.github.nscala_time.time.Imports._
import org.joda.time.DateTime
import scalax.collection.GraphEdge.DiEdge
import scalax.collection.io.dot.{DotRootGraph, _}
import scalax.collection.mutable
import scalax.collection.Graph

/**
  * Created by tobi on 12.11.16.
  */
case class Screening(movie: Movie, time: DateTime, location: Cinema) {
    def start: DateTime = time

    def end: DateTime = time + movie.duration

    def transit(other: Screening) : Duration = location.transit(other.location)

    def isReachable(from: Screening) : Boolean = (from.end + transit(from)) isBefore start

    override def toString: String = {
        val hoursMinuteFormatter = DateTimeFormat.forPattern("d-HH:mm")
        s"[${movie.name} @ ${hoursMinuteFormatter.print(time)} @ $location]"
    }
}

class ScreeningGraph (screenings: List[Screening]) {
    private val startNode = Screening(Movie("startNode", 0.minutes), DateTime.now, DummyLocation)
    private val endNode = Screening(Movie("endNode", 0.minutes), DateTime.now, DummyLocation)

    private val graph: Graph[Screening, DiEdge] = buildGraph(screenings)

    private def buildGraph(screenings: List[Screening]) = {
        val buildingGraph = mutable.Graph[Screening, DiEdge]()

        val screeningDays = screenings.map(_.time.getDayOfYear).toSet

        val screeningByCinema = screenings.map(s => s.location -> screenings.filter(_.location == s.location).sortBy(_.time)).toMap

        for ((cinema, filmlist) <- screeningByCinema) {
            val modifiedFilmList = startNode +: filmlist :+ endNode
            val consecutiveFilms = modifiedFilmList.dropRight(1).zip(modifiedFilmList.tail)

            for ((first, follower) <- consecutiveFilms) {
                val edge = DiEdge(first, follower)
                println("Adding edge " + edge)
                buildingGraph += edge
            }

            for ((otherCinema, otherFilmList) <- screeningByCinema if cinema != otherCinema) {
                for (film <- filmlist) {
                    val reachableFilms = otherFilmList.filter(_.isReachable(film))
                    if (reachableFilms.nonEmpty) {
                        val reachableFilm = reachableFilms.minBy(_.time)
                        println(reachableFilm + " is first in " + reachableFilm.location + " reachable form " + film)
                        buildingGraph += DiEdge(film, reachableFilm)
                    }
                }
            }
        }
        buildingGraph
    }


    def toDot : String = {
        val dotRoot = DotRootGraph(directed = true,Some(Id("IFFMH")))

        def edgeTransformer(innerEdge: Graph[Screening, DiEdge]#EdgeT): Option[(DotGraph,DotEdgeStmt)] =
            innerEdge.edge match {
                case DiEdge(source, target) => Some(dotRoot, DotEdgeStmt(NodeId(source.toString()), NodeId(target.toString())))
            }

        graph.toDot(dotRoot, edgeTransformer)
    }
}

import com.github.nscala_time.time.Imports._
import org.joda.time.DateTime
import scalax.collection.GraphEdge.DiEdge
import scalax.collection.io.dot.{DotRootGraph, _}
import scalax.collection.Graph
import scala.collection.mutable

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
        val buildingGraph = scalax.collection.mutable.Graph[Screening, DiEdge]()

        val screeningDays = screenings.map(_.time.getDayOfYear).toSet

        val screeningByCinema = screenings.map(s => s.location -> screenings.filter(_.location == s.location).sortBy(_.time)).toMap

        for ((cinema, filmlist) <- screeningByCinema) {
            val modifiedFilmList = startNode +: filmlist :+ endNode
            val consecutiveFilms = modifiedFilmList.dropRight(1).zip(modifiedFilmList.tail)

            for ((first, follower) <- consecutiveFilms) {
                buildingGraph += DiEdge(first, follower)
            }

            for ((otherCinema, otherFilmList) <- screeningByCinema if cinema != otherCinema) {
                for (film <- filmlist) {
                    val reachableFilms = otherFilmList.filter(_.isReachable(film))
                    if (reachableFilms.nonEmpty) {
                        val reachableFilm = reachableFilms.minBy(_.time)
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

    private def modDijkstra() : List[Screening] = {
        val distance = mutable.Map[Screening, Int]()
        val pred = mutable.Map[Screening, Option[Screening]]()
        val Q: mutable.Set[Screening] = mutable.Set(graph.nodes).flatten
        for (node <- Q) {
            distance(node) = 1
            pred(node) = None
        }
        distance(startNode) = 0

        def moviesInPath(path: Map[Screening, Option[Screening]]) : Set[Movie] = {
            path.values.filter(_.isDefined).map(_.get.movie).toSet
        }

        def update_dist(u: Screening, v: Screening) : Unit = {
            val weight : Int = if(moviesInPath(pred.toMap).contains(v.movie)) 0 else 1
            val alternative = distance(u) - weight  //distance between two screenings is always 1 in this implementation
            if (alternative < distance(v)) {
                distance(v) = alternative
                pred(v) = Some(u)
             }
        }

        while (Q.nonEmpty) {
            val u = distance.filter(e => Q.contains(e._1)).toList.minBy(_._2)._1
            Q.remove(u) //TODO: possible speedup: stop here when end is reached!
            for(v <- graph.get(u).neighbors if Q.contains(v)) {
                update_dist(u, v)
            }
        }

        val path : mutable.MutableList[Screening] = mutable.MutableList()
        var u = endNode

        while(pred(u).isDefined) {
            path += u
            u = pred(u).get
        }

        println("Shortest Path has length " + distance(endNode))

        path.tail.reverse.toList
    }

    def longestPath() : List[Screening] = {
        //Idea: Use shortest path, edge weight is -1 if edge end node is in path, 0 otherwise
        //resulting path should contain the most screenings
        modDijkstra()
    }
}

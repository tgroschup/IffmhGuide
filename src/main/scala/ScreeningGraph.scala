import org.joda.time.{DateTime, Duration}
import com.github.nscala_time.time.Imports._

import scalax.collection.GraphEdge.DiEdge
import scalax.collection.mutable.Graph

/**
  * Created by tobi on 12.11.16.
  */
case class Screening(name: String, time: DateTime, location: Cinema, duration: Duration)

object ScreeningGraph {
  val graph = Graph[Screening, DiEdge]()

  def buildGraph(screenings: List[Screening]) = {
    val screeningsByLocationAndTime: Map[(Cinema, DateTime), Screening] = screenings.map(s => {
      (s.location, s.time) -> s
    }).toMap

    val startNode = Screening("start", DateTime.now, DummyLocation, 0.minutes)
    val startScreenings = for(location <- List(Kino1, Kino2, N1Kino1, N1Kino2, Atlantins)) yield {
      screenings.filter(_.location == location).minBy(_.time)
    }

    for(start <- startScreenings) {
      graph += DiEdge(startNode, start)
    }

  }
}

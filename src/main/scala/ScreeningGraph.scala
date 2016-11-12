import org.joda.time.DateTime

import scala.collection.mutable
import scalax.collection.GraphEdge.DiEdge
import scalax.collection.mutable.Graph

/**
  * Created by tobi on 12.11.16.
  */
case class Screening(name: String, time: DateTime, location: Cinema)

object ScreeningGraph {
  val graph = Graph[Screening, DiEdge]()

  def buildGraph(screenings: List[Screening]) = {
    val screeningsByLocationAndTime: Map[(Cinema, DateTime), Screening] = screenings.map(s => {
      (s.location, s.time) -> s
    }).toMap
  }

  val startNode = Screening("start", DateTime.now, DummyLocation)

  graph
}

import org.joda.time.DateTime
import com.github.nscala_time.time.Imports._
/**
  * Created by tobi on 12.11.16.
  */
object IffmhGuide extends App {

  val screeningList = List[Screening](
    Screening(Movie("Dog Days", 85.minutes) , DateTime.now + 2.hours, N1Kino1),
    Screening(Movie("Deine Mudda sei Gsischt", 13.minutes) , DateTime.now + 2.hours + 15.minutes, N1Kino2),
    Screening(Movie("kung Foo", 100.minutes), DateTime.now + 2.hours, Atlantis),
    Screening(Movie("Abgezockt ist abgezockt", 90.minutes), DateTime.now + 2.hours, Kino1),
    Screening(Movie("Fight Club", 20.minutes), DateTime.now + 30.minutes, Kino2),

    Screening(Movie("Dog Days", 85.minutes) , DateTime.now + 1.days + 2.hours, N1Kino2),
    Screening(Movie("Deine Mudda sei Gsischt", 13.minutes) , DateTime.now +1.days + 2.hours + 15.minutes, N1Kino1),
    Screening(Movie("kung Foo", 100.minutes), DateTime.now + 1.days + 2.hours, Kino1),
    Screening(Movie("Abgezockt ist abgezockt", 90.minutes), DateTime.now +1.days + 2.hours, Kino2),
    Screening(Movie("Fight Club", 20.minutes), DateTime.now +1.days + 30.minutes, Atlantis)
  )


  ScreeningGraph.buildGraph(screeningList)
}

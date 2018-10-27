import org.joda.time.DateTime
import com.github.nscala_time.time.Imports._
import org.joda.time.format.DateTimeFormatter
import org.scalatest.FunSuite

/**
  * Created by tobi on 16.11.16.
  */

import scalax.collection.io.dot._

class IntegrationTests extends FunSuite {

  test("build a graph"){

    val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
    val start = formatter.parseDateTime("10-11-2017")

      val dogDays = Movie("Dog Days", 85.minutes)
      val deiMudda = Movie("Deine Mudda sei Gsischt", 13.minutes)
      val kungFoo = Movie("Kung Foo", 100.minutes)
      val abgezockt = Movie("Abgezockt ist abgezockt", 90.minutes)

    val screeningList = List[Screening](
      Screening(dogDays , start + 17.hours, Kino1),
      Screening(deiMudda , start + 19.hours, Kino1),
      Screening(kungFoo, start + 17.hours, Kino2),
      Screening(abgezockt, start + 19.hours, Kino2),
      /*Screening(Movie("Negeralarm im Mädchendarm", 20.minutes), DateTime.now + 30.minutes, Kino2),*/

      Screening(dogDays , start + 1.days + 17.hours, Kino1),
      Screening(deiMudda , start +1.days + 17.hours + 15.minutes, Kino2),
      Screening(kungFoo, start + 1.days + 19.hours, Kino1),
      Screening(abgezockt, start +1.days + 19.hours, Kino2)/*
      Screening(Movie("Negeralarm im Mädchendarm", 20.minutes), DateTime.now +1.days + 30.minutes, Atlantis)*/
    )

    ScreeningGraph.buildGraph(screeningList)

    print(ScreeningGraph.toDot())
  }

}

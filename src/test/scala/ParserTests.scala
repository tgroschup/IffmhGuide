import org.scalatest.FunSuite
import com.github.nscala_time.time.Imports._


class ParserTests extends FunSuite {
    test("simple line") {
        val movieDefiniton = "Movie Name;90;10-1100-m1;12-1212m2;13-2359-a;14-1600-h1;18-1900-h2;"

        val m = Movie("Movie Name", 90.minutes)
        val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
        val start = formatter.parseDateTime("01-11-2019")
        val refScreening = List[Screening](Screening(m, start+10.days+11.hours, N1Kino1),
                                            Screening(m, start+12.days+12.hours+12.minutes, N1Kino2),
                                            Screening(m, start+13.days+23.hours+59.minutes, Atlantis),
                                            Screening(m, start+14.days+16.hours, Kino1),
                                            Screening(m, start+18.days+19.hours, Kino2))

        val result = ScreeningsParser.screenings(movieDefiniton)

        assertResult(refScreening)(result)
    }

}

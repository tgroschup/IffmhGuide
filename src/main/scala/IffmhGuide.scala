import org.joda.time.DateTime
import com.github.nscala_time.time.Imports._
/**
  * Created by tobi on 12.11.16.
  */
class IffmhGuide extends App {

  val screeningList = List[Screening](
    Screening("Dog Days", DateTime.now + 2.hours, N1Kino1, 85.minutes)
  )

}

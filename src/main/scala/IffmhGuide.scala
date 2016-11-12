import org.joda.time.DateTime

/**
  * Created by tobi on 12.11.16.
  */
class IffmhGuide extends App {

  val screeningList = List[Screening](
    Screening("Dog Days", DateTime.now + 2.hour, N1Kino1)
  )

}

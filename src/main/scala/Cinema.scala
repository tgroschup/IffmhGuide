import com.github.nscala_time.time.Imports._

/**
  * Created by tobi on 12.11.16.
  */
sealed abstract class Cinema {
    def transit[T <: Cinema](other: T) : Duration
}

sealed abstract class CinemaMa extends Cinema {
    override def transit[T <: Cinema](other: T): Duration =
        other match {
            case _: CinemaMa => 5.minutes
            case _: CinemaHD => 120.minutes
            case _ => 0.minutes
        }
}
sealed abstract class CinemaHD extends Cinema {
    override def transit[T <: Cinema](other: T): Duration = {
        val t = other match {
            case _: CinemaHD => 5.minutes
            case _: CinemaMa => 120.minutes
            case _ => 0.minutes
        }
        //println("transit from " + toString + " to " + other.toString + " is " + t.getMinutes.toString)
        t
    }
}
case object DummyLocation extends Cinema {
    override def transit[T <: Cinema](other: T) : Duration = 0.minutes
}
case object N1Kino1 extends CinemaMa
case object N1Kino2 extends CinemaMa
case object Atlantis extends CinemaMa

case object Kino1 extends CinemaHD
case object Kino2 extends CinemaHD

object Cinema {
  def apply(name: String): Cinema = name match {
    case "N1Kino1" => N1Kino1
    case "N1Kino2" => N1Kino2
    case "Atlantis" => Atlantis
    case "Kino1" => Kino1
    case "Kino2" => Kino2
    case unknown => throw new  Exception(s"Unknown cinema $unknown")
  }
}
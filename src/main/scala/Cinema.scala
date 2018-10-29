import com.github.nscala_time.time.Imports
import com.github.nscala_time.time.Imports._

/**
  * Created by tobi on 12.11.16.
  */

sealed abstract class Cinema {
    def transit[T <: Cinema](other: T) : Duration
}

sealed abstract class CinemaMa extends Cinema {
    def transit[T <: Cinema](other: T): Duration =
        other match {
            case _: CinemaMa => 10.minutes //espcecially n1 <-> atlantis
            case _: CinemaHD => 120.minutes
            case _ => 0.minutes
        }
}
sealed abstract class CinemaHD extends Cinema {
    def transit[T <: Cinema](other: T): Duration = {
        other match {
            case _: CinemaHD => 1.minutes
            case _: CinemaMa => 120.minutes
            case _ => 0.minutes
        }
    }
}
case object DummyLocation extends Cinema {
    override def transit[T <: Cinema](other: T) : Duration = 0.minutes
}

sealed abstract class CinemaN1 extends CinemaMa {
    override def transit[T <: Cinema](other: T): Imports.Duration =
        other match {
            case _: CinemaN1 => 1.minutes
            case default => super.transit(other)
        }
}
case object N1Kino1 extends CinemaN1
case object N1Kino2 extends CinemaN1
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
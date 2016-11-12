/**
  * Created by tobi on 12.11.16.
  */
sealed abstract class Cinema

sealed abstract class CinemaMa extends Cinema
sealed abstract class CinemaHD extends Cinema

case object DummyLocation extends Cinema
case object N1Kino1 extends CinemaMa
case object N1Kino2 extends CinemaMa
case object Atlantins extends CinemaMa

case object Kino1 extends CinemaHD
case object Kino2 extends CinemaHD

object Cinema {
  def apply(name: String) = name match {
    case "N1Kino1" => N1Kino1
    case "N1Kino2" => N1Kino2
    case "Atlantis" => Atlantins
    case "Kino1" => Kino1
    case "Kino2" => Kino2
    case unknown => throw new  Exception(s"Unknown cinema $unknown")
  }
}
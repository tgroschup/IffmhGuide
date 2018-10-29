import scala.util.parsing.combinator.RegexParsers

object ScreeningsParser extends RegexParsers {

    val screeningList = scala.collection.mutable.MutableList[Screening]()

    def name = "[a-zA-Z0-9_-?,!#()]+".r

    def time = ""

    def screening = name

    def screenings(input: String) : List[Screening] = {
        screeningList.clear
        parse(screening, input) match {
            case Success(_, _) => screeningList.toList
            case Failure(msg, _) => throw new Exception("Parsing failed here:\n" + msg)
            case Error(msg, _) => throw new Exception("ERROR! " + msg)
        }
    }
}

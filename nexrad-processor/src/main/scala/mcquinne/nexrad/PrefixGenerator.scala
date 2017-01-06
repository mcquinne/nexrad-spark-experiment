package mcquinne.nexrad

object PrefixGenerator {

  def generate(full: Boolean = false): Seq[String] = {
    years(full).flatMap(year => {
      months(full).flatMap(month => {
        days(full).flatMap(day => {
          stations(full).map(station => {
            s"$year/$month/$day/$station"
          })
        })
      })
    })
  }

  private def years(full: Boolean = false): Seq[String] = {
    val range = if (full) 1991 to 2017 else 2000 to 2000
    range.map(y => f"$y%04d")
  }

  private def months(full: Boolean = false): Seq[String] = {
    val range = if (full) 1 to 12 else 1 to 1
    range.map(m => f"$m%02d")
  }

  private def days(full: Boolean = false): Seq[String] = {
    val range = if (full) 1 to 31 else 1 to 1
    range.map(m => f"$m%02d")
  }

  private def stations(full: Boolean = false): Seq[String] = {
    Seq("KABR") // TODO - full list of stations?
  }

}

package journeyplan

class SubwayMap(private val map: List<Segment>) {
  fun routesFrom(
    origin: Station,
    destination: Station,
    optimisingFor: (Route) -> Int = Route::duration
  ): List<Route> {
    fun routesFrom(
      origin: Station,
      destination: Station,
      currLine: Line?,
      visited: Set<Station>
    ): List<Route> {
      if (origin == destination) {
        return emptyList()
      }

      val routes = mutableListOf<Route>()

      for (segment in map) {
        if (!segment.line.isWorking) {
          continue
        }

        val from = segment.from
        val to = segment.to
        val line = segment.line
        // Stops passengers from changing lines at a closed station
        if (line != currLine && !from.isOpen) {
          continue
        }

        if (from == origin && !visited.contains(from)) {
          val subRoutes = routesFrom(to, destination, line, visited + from)
          routes += if (to == destination) {
            listOf(Route(listOf(segment)))
          } else {
            subRoutes.map { subRoute -> Route(listOf(segment) + subRoute.segments) }
          }
        }
      }
      routes.sortWith { route1, route2 -> optimisingFor(route1).compareTo(optimisingFor(route2)) }
      return routes
    }
    return routesFrom(origin, destination, null, HashSet())
  }
}

fun londonUnderground(): SubwayMap {
  // Stations
  val southKensington = Station("South Kensington")
  val gloucesterRoad = Station("Gloucester Road")
  val highStreetKensington = Station("High Street Kensington")
  val nottingHillGate = Station("Notting Hill Gate")
  val shepherdsBush = Station("Shepherds Bush")

  // Lines
  val piccadilly = Line("Piccadilly")
  val district = Line("District")
  val circle = Line("Circle")
  val central = Line("Central")

  return SubwayMap(
    listOf(
      Segment(southKensington, gloucesterRoad, district, 3),
      Segment(southKensington, gloucesterRoad, circle, 3),
      Segment(southKensington, gloucesterRoad, piccadilly, 5),
      Segment(gloucesterRoad, highStreetKensington, circle, 5),
      Segment(highStreetKensington, nottingHillGate, circle, 5),
      Segment(nottingHillGate, shepherdsBush, central, 7)
    )
  )
}

class Route(val segments: List<Segment>) {
  fun numChanges(): Int {
    if (segments.isEmpty()) {
      return 0
    }
    var line = segments[0].line
    var numChanges = 0
    for (segment in segments) {
      if (line != segment.line) {
        numChanges++
        line = segment.line
      }
    }
    return numChanges
  }

  fun duration(): Int {
    if (segments.isEmpty()) {
      return 0
    }
    var duration = 0
    for (segment in segments) {
      duration += segment.duration
    }
    return duration
  }

  override fun toString(): String {
    if (segments.isEmpty()) {
      return ""
    }

    val origin = segments[0].from
    val destination = segments[segments.size - 1].to
    val duration = duration()
    val numChanges = numChanges()

    val sb = StringBuilder("$origin to $destination - $duration minutes, $numChanges changes\n")

    var start = origin
    var end = segments[0].to
    var line = segments[0].line

    for (segment in segments) {
      if (line == segment.line) {
        end = segment.to
        continue
      }
      sb.append(" - $start to $end by $line\n")
      start = segment.from
      end = segment.to
      line = segment.line
    }
    sb.append(" - $start to $end by $line")
    return sb.toString()
  }
}

fun main() {
  val map = londonUnderground()
  println(map.routesFrom(Station("South Kensington"), Station("Shepherds Bush")))
}

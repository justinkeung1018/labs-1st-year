package journeyplan

class Station(val name: String) {
  var isOpen = true

  fun close() {
    isOpen = false
  }

  fun open() {
    isOpen = true
  }

  override fun toString(): String = name

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null) return false
    if (other !is Station) return false
    return name == other.name
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }
}

class Line(val name: String) {
  var isWorking = true

  fun suspend() {
    isWorking = false
  }

  fun resume() {
    isWorking = true
  }

  override fun toString(): String = "$name Line"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null) return false
    if (other !is Line) return false
    return name == other.name
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }
}

class Segment(val from: Station, val to: Station, val line: Line, val duration: Int)

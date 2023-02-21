package museumvisit;

import java.util.Optional;

public class Turnstile {

  private final MuseumSite originRoom;
  private final MuseumSite destinationRoom;

  public Turnstile(MuseumSite originRoom, MuseumSite destinationRoom) {
    assert !originRoom.equals(destinationRoom);
    this.originRoom = originRoom;
    this.destinationRoom = destinationRoom;
  }

  public Optional<MuseumSite> passToNextRoom() {
    MuseumSite room1, room2;
    // Since the rooms have different names (as asserted by constructor and the
    // implementation of equals), calling compareTo on their names will never return 0,
    // as opposed to comparing their hashcodes, since two rooms with different names
    // may have the same hashcode.
    if (originRoom.getName().compareTo(destinationRoom.getName()) < 0) {
      room1 = originRoom;
      room2 = destinationRoom;
    } else {
      room1 = destinationRoom;
      room2 = originRoom;
    }
    if (destinationRoom.hasAvailability()) {
      // Synchronise both rooms since the visitor should not be able to move
      // if either exiting or entering fails
      synchronized (room1) {
        synchronized (room2) {
          originRoom.exit();
          destinationRoom.enter();
        }
      }
      return Optional.of(destinationRoom);
    }
    return Optional.empty();
  }

  public MuseumSite getOriginRoom() {
    return originRoom;
  }

  public MuseumSite getDestinationRoom() {
    return destinationRoom;
  }
}

package museumvisit;

import java.util.Optional;

public class Turnstile {

  private final MuseumSite originRoom;
  private final MuseumSite destinationRoom;

  public Turnstile(MuseumSite originRoom, MuseumSite destinationRoom) {
    assert !originRoom.equals(destinationRoom);
    this.originRoom = originRoom;
    this.destinationRoom = destinationRoom;
    // complete here if needed
  }

  public Optional<MuseumSite> passToNextRoom() {
    if (destinationRoom.hasAvailability()) {
      originRoom.exit();
      destinationRoom.enter();
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

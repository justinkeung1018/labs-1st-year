package museumvisit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Test;

public class TurnstileTest {

  @Test
  public void checkEnterExitProtocol() {
    ExhibitionRoom roomFrom = new ExhibitionRoom("roomFrom", 1);
    ExhibitionRoom roomTo = new ExhibitionRoom("roomTo", 1);

    assertTrue(roomFrom.hasAvailability());
    assertTrue(roomTo.hasAvailability());
    roomFrom.enter();
    assertEquals(1, roomFrom.getOccupancy());
    assertEquals(0, roomTo.getOccupancy());

    Turnstile turnstile = new Turnstile(roomFrom, roomTo);
    Optional<MuseumSite> nextRoom = turnstile.passToNextRoom();
    assertTrue(nextRoom.isPresent());

    assertEquals(0, roomFrom.getOccupancy());
    assertEquals(1, roomTo.getOccupancy());

    assertFalse(roomTo.hasAvailability());
    roomFrom.enter();
    assertEquals(1, roomFrom.getOccupancy());

    nextRoom = turnstile.passToNextRoom();
    assertFalse(nextRoom.isPresent());
    assertEquals(1, roomFrom.getOccupancy());
    assertEquals(1, roomTo.getOccupancy());
  }
}

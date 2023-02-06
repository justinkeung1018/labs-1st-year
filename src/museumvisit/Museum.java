package museumvisit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Museum {

  private final Entrance entrance;
  private final Exit exit;
  private final Set<MuseumSite> sites;

  public Museum(Entrance entrance, Exit exit, Set<MuseumSite> sites) {
    this.entrance = entrance;
    this.exit = exit;
    this.sites = sites;
  }

  public static void main(String[] args) {
    final int numberOfVisitors = 100; // Your solution has to work with any number of visitors
    final Museum museum = buildSimpleMuseum(); // buildLoopyMuseum();

    // create the threads for the visitors and get them moving
    List<Thread> threads = new ArrayList<>();
    for (int i = 1; i <= numberOfVisitors; i++) {
      Thread thread = new Thread(new Visitor(String.valueOf(i), museum.getEntrance()));
      threads.add(thread);
      thread.start();
    }

    // wait for them to complete their visit
    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        System.out.println("Error");
      }
    }

    // Checking no one is left behind
    if (museum.getExit().getOccupancy() == numberOfVisitors) {
      System.out.println("\nAll the visitors reached the exit\n");
    } else {
      System.out.println(
          "\n"
              + (numberOfVisitors - museum.getExit().getOccupancy())
              + " visitors did not reach the exit. Where are they?\n");
    }

    System.out.println("Occupancy status for each room (should all be zero, but the exit site):");
    System.out.println("Entrance final occupancy: " + museum.getEntrance().getOccupancy());
    museum
        .getSites()
        .forEach(
            s -> {
              System.out.println("Site " + s.getName() + " final occupancy: " + s.getOccupancy());
            });
  }

  public static Museum buildSimpleMuseum() {
    Entrance entrance = new Entrance();
    ExhibitionRoom exhibitionRoom = new ExhibitionRoom("Exhibition", 10);
    Exit exit = new Exit();

    entrance.addExitTurnstile(new Turnstile(entrance, exhibitionRoom));
    exhibitionRoom.addExitTurnstile(new Turnstile(exhibitionRoom, exit));

    Set<MuseumSite> sites = new HashSet<>();
    sites.add(exhibitionRoom);

    return new Museum(entrance, exit, sites);
  }

  public static Museum buildLoopyMuseum() {
    Entrance entrance = new Entrance();
    ExhibitionRoom venomRoom = new ExhibitionRoom("Venom KillerAndCure", 10);
    ExhibitionRoom whalesRoom = new ExhibitionRoom("Whales", 10);
    Exit exit = new Exit();

    entrance.addExitTurnstile(new Turnstile(entrance, venomRoom));
    venomRoom.addExitTurnstile(new Turnstile(venomRoom, whalesRoom));
    venomRoom.addExitTurnstile(new Turnstile(venomRoom, exit));
    whalesRoom.addExitTurnstile(new Turnstile(whalesRoom, venomRoom));

    Set<MuseumSite> sites = new HashSet<>();
    sites.add(venomRoom);
    sites.add(whalesRoom);

    return new Museum(entrance, exit, sites);
  }

  public Entrance getEntrance() {
    return entrance;
  }

  public Exit getExit() {
    return exit;
  }

  public Set<MuseumSite> getSites() {
    return sites;
  }
}

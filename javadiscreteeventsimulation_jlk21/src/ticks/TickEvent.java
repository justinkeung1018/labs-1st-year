package ticks;

import simulation.Event;

public class TickEvent implements Event<Ticks> {
  @Override
  public void invoke(Ticks simulation) {
    simulation.schedule(new TickEvent(), 1.0);
    System.out.println("Tick at: " + simulation.getCurrentTime());
  }
}

package ticks;

import simulation.Event;
import simulation.Simulation;

public class TickEvent implements Event {
  @Override
  public void invoke(Simulation simulation) {
    simulation.schedule(new TickEvent(), 1.0);
    System.out.println("Tick at: " + simulation.getCurrentTime());
  }
}

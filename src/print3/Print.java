package print3;

import simulation.Event;
import simulation.Simulation;

class Print implements Event {

  private final int n;

  public Print(int n) {
    this.n = n;
  }

  @Override
  public void invoke(Simulation simulation) {
    System.out.println("Event " + n + " invoked at time " + simulation.getCurrentTime());
  }
}

package ssq;

import simulation.Event;

public class DepartureEvent implements Event<SingleServerQueue> {
  @Override
  public void invoke(SingleServerQueue simulation) {
    simulation.setQueueLength(simulation.getQueueLength() - 1);
    if (simulation.getQueueLength() > 0) {
      simulation.schedule(new DepartureEvent(), simulation.getServiceTime());
    }
    System.out.println("Departure at: " + simulation.getCurrentTime() + ", new population = " +
        simulation.getQueueLength());
  }
}

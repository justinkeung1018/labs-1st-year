package ssq;

import simulation.Event;

public class ArrivalEvent implements Event<SingleServerQueue> {
  @Override
  public void invoke(SingleServerQueue simulation) {
    simulation.schedule(new ArrivalEvent(), simulation.getInterArrivalTime());
    if (simulation.getQueueLength() == 0) {
      simulation.schedule(new DepartureEvent(), simulation.getServiceTime());
    }
    simulation.setQueueLength(simulation.getQueueLength() + 1);
    System.out.println("Arrival at " + simulation.getCurrentTime() + ", new population = " +
        simulation.getQueueLength());
  }
}

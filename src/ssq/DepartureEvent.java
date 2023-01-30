package ssq;

import simulation.Event;
import simulation.Simulation;

public class DepartureEvent implements Event {
  @Override
  public void invoke(Simulation simulation) {
    SingleServerQueue ssq = (SingleServerQueue) simulation;
    ssq.setQueueLength(ssq.getQueueLength() - 1);
    if (ssq.getQueueLength() > 0) {
      ssq.schedule(new DepartureEvent(), ssq.getServiceTime());
    }
    System.out.println("Departure at: " + ssq.getCurrentTime() + ", new population = " + ssq.getQueueLength());
  }
}

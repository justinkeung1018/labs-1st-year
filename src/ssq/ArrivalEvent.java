package ssq;

import simulation.Event;
import simulation.Simulation;

public class ArrivalEvent implements Event {
  @Override
  public void invoke(Simulation simulation) {
    SingleServerQueue ssq = (SingleServerQueue) simulation;
    ssq.schedule(new ArrivalEvent(), ssq.getInterArrivalTime());
    if (ssq.getQueueLength() == 0) {
      ssq.schedule(new DepartureEvent(), ssq.getServiceTime());
    }
    ssq.setQueueLength(ssq.getQueueLength() + 1);
    System.out.println("Arrival at: " + ssq.getCurrentTime() + ", new population = " + ssq.getQueueLength());
  }
}

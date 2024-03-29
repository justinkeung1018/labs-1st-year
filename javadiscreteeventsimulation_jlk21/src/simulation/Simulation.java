package simulation;

import java.util.PriorityQueue;
import java.util.Queue;

public class Simulation<S> {
  private double currentTime;
  private final Queue<ScheduledEvent<S>> scheduledEvents;

  public Simulation() {
    currentTime = 0.0;
    scheduledEvents = new PriorityQueue<>();
  }

  protected boolean stop() {
    return false;
  }

  public void schedule(Event<S> event, double offset) {
    scheduledEvents.offer(new ScheduledEvent<>(event, currentTime + offset));
  }

  public void simulate() {
    while (!scheduledEvents.isEmpty()) {
      ScheduledEvent<S> scheduledEvent = scheduledEvents.poll();
      assert scheduledEvent != null;
      currentTime = scheduledEvent.getTime();
      if (stop()) {
        break;
      }
      scheduledEvent.getEvent().invoke(this.getState());
    }
  }

  public double getCurrentTime() {
    return currentTime;
  }

  public S getState() {
    return null;
  }
}

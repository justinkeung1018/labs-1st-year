package simulation;

public class ScheduledEvent implements Comparable<ScheduledEvent> {
  private final Event event;
  private final double time;

  public ScheduledEvent(Event event, double time) {
    if (event == null) {
      throw new IllegalArgumentException("Event must not be null.");
    }
    this.event = event;
    this.time = time;
  }

  @Override
  public int compareTo(ScheduledEvent other) {
    return Double.compare(time, other.time);
  }

  public double getTime() {
    return time;
  }

  public Event getEvent() {
    return event;
  }
}

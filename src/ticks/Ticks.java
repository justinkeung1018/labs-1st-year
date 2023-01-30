package ticks;

import simulation.Simulation;

public class Ticks extends Simulation<Ticks> {
  private final double duration;

  public Ticks(double duration) {
    super();
    this.duration = duration;
  }

  @Override
  protected boolean stop() {
    return getCurrentTime() >= duration;
  }

  @Override
  public Ticks getState() {
    return this;
  }

  public static void main(String[] args) {
    double duration = Double.parseDouble(args[0]);
    Simulation<Ticks> simulation = new Ticks(duration);
    simulation.schedule(new TickEvent(), 1.0);
    simulation.simulate();
  }
}

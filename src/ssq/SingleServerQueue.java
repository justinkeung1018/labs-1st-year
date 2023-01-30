package ssq;

import java.util.Random;
import simulation.Simulation;

public class SingleServerQueue extends Simulation {
  private final double serviceTime = 0.25;
  private final double duration;
  private final Random generator;
  private int queueLength;

  public SingleServerQueue(long seed, double duration) {
    super();
    this.duration = duration;
    this.generator = new Random(seed);
    this.queueLength = 0;
  }

  @Override
  protected boolean stop() {
    return getCurrentTime() >= duration;
  }

  protected double getInterArrivalTime() {
    return generator.nextDouble();
  }

  protected double getServiceTime() {
    return serviceTime;
  }

  protected void setQueueLength(int queueLength) {
    this.queueLength = queueLength;
  }

  protected int getQueueLength() {
    return queueLength;
  }

  public static void main(String[] args) {
    long seed = Long.parseLong(args[0]);
    double duration = Double.parseDouble(args[1]);
    SingleServerQueue ssq = new SingleServerQueue(seed, duration);
    ssq.schedule(new ArrivalEvent(), ssq.getInterArrivalTime());
    ssq.simulate();
    System.out.println("SIMULATION COMPLETE");
  }
}

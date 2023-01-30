package ssq;

import java.util.Random;
import simulation.Simulation;

public class SingleServerQueue extends Simulation<SingleServerQueue> {
  private final double serviceTime = 0.25;
  private final double duration;
  private final Random generator;
  private int queueLength;
  private double timeWhenQueueLengthLastChanged;
  private double weightedQueueLengthSum;

  public SingleServerQueue(long seed, double duration) {
    super();
    this.duration = duration;
    this.generator = new Random(seed);
    this.queueLength = 0;
    this.timeWhenQueueLengthLastChanged = 0.0;
    this.weightedQueueLengthSum = 0.0;
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
    addWeightedQueueLength(getCurrentTime());
    this.queueLength = queueLength;
  }

  private void addWeightedQueueLength(double time) {
    double weightedQueueLength = queueLength * (time - timeWhenQueueLengthLastChanged);
    weightedQueueLengthSum += weightedQueueLength;
    timeWhenQueueLengthLastChanged = time;
  }

  protected int getQueueLength() {
    return queueLength;
  }

  private double getMeanQueueLength() {
    return weightedQueueLengthSum / duration;
  }

  @Override
  public SingleServerQueue getState() {
    return this;
  }

  public static void main(String[] args) {
    long seed = Long.parseLong(args[0]);
    double duration = Double.parseDouble(args[1]);
    Simulation<SingleServerQueue> simulation = new SingleServerQueue(seed, duration);
    SingleServerQueue ssq = simulation.getState();
    simulation.schedule(new ArrivalEvent(), ssq.getInterArrivalTime());
    simulation.simulate();
    ssq.addWeightedQueueLength(duration);
    System.out.println("SIMULATION COMPLETE - the mean queue length was " +
        ssq.getMeanQueueLength());
  }
}

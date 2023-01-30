package print3;

import simulation.Simulation;

public class Print3 extends Simulation {

  @Override
  protected boolean stop() {
    return false;
  }

  public static void main(String args[]) {
    Print3 p = new Print3();
    p.schedule(new Print(1), 7.2);
    p.schedule(new Print(2), 11.6);
    p.schedule(new Print(3), 2.9);
    p.simulate();
  }
}

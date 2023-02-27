package socialnetwork;

import socialnetwork.domain.Backlog;
import socialnetwork.domain.Board;
import socialnetwork.domain.Worker;

public class Main {

  public static void main(String[] args) {
    // Implement logic here following the steps described in the specs
    Backlog backlog = new TaskBacklog();
    SocialNetwork socialNetwork = new SocialNetwork(backlog);

    Worker w1 = new Worker(backlog);
    Worker w2 = new Worker(backlog);
    Worker w3 = new Worker(backlog);

    User u1 = new User("A", socialNetwork);
    User u2 = new User("B", socialNetwork);

    socialNetwork.register(u1, new CoarseSyncBoard());
    socialNetwork.register(u2, new CoarseSyncBoard());

    u1.start();
    u2.start();
    w1.start();
    w2.start();
    w3.start();

    try {
      u1.join();
      u2.join();
    } catch (InterruptedException e) {
      System.out.println("Users interrupted");
    }

    w1.interrupt();
    w2.interrupt();
    w3.interrupt();

    try {
      w1.join();
      w2.join();
      w3.join();
    } catch (InterruptedException e) {
      System.out.println("Workers interrupted");
    }
  }
}

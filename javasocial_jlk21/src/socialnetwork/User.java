package socialnetwork;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import socialnetwork.domain.Board;
import socialnetwork.domain.Message;

public class User extends Thread {

  private static final AtomicInteger nextId = new AtomicInteger(0);

  protected final SocialNetwork socialNetwork;
  private final int id;
  private final String name;
  private final Random generator;

  public User(String username, SocialNetwork socialNetwork) {
    this.name = username;
    this.id = User.nextId.getAndIncrement();
    this.socialNetwork = socialNetwork;
    this.generator = new Random();
  }

  public int getUserId() {
    return id;
  }

  @Override
  public void run() {
    Set<User> allUsers = socialNetwork.getAllUsers();
    Set<User> messageRecipients = new HashSet<>();
    for (User user : allUsers) {
      if (generator.nextBoolean()) {
        messageRecipients.add(user);
      }
    }
    socialNetwork.postMessage(this, messageRecipients, "Hi");
    Board board = socialNetwork.userBoard(this);
    for (Message message : board.getBoardSnapshot()) {
      if (generator.nextBoolean()) {
        socialNetwork.deleteMessage(message);
      }
    }
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  @Override
  public int hashCode() {
    return id;
  }
}

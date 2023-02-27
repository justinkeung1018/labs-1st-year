package socialnetwork.domain;

import java.util.Optional;

public class Worker extends Thread {

  private final Backlog backlog;
  private boolean interrupted = false;

  public Worker(Backlog backlog) {
    this.backlog = backlog;
  }

  @Override
  public void run() {
    while (!interrupted) {
      Optional<Task> task = backlog.getNextTaskToProcess();
      if (task.isPresent()) {
        process(task.get());
      } else {
        try {
          sleep(200);
        } catch (InterruptedException e) {
          interrupted = true;
        }
      }
    }
  }

  public void interrupt() {
    this.interrupted = true;
  }

  public void process(Task nextTask) {
    Board board = nextTask.getBoard();
    Message message = nextTask.getMessage();
    switch (nextTask.getCommand()) {
      case POST -> board.addMessage(message);
      case DELETE -> {
        boolean deleted = board.deleteMessage(message);
        if (!deleted) {
          backlog.add(nextTask);
        }
      }
    }
  }
}

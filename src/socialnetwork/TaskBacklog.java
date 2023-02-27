package socialnetwork;

import socialnetwork.domain.Backlog;
import socialnetwork.domain.Task;

import java.util.Optional;

public class TaskBacklog implements Backlog {
  private final CoarseOrderedLinkedList<Task> tasks;

  public TaskBacklog() {
    tasks = new CoarseOrderedLinkedList<>();
  }

  @Override
  public synchronized boolean add(Task task) {
    return tasks.offerLast(task);
  }

  @Override
  public synchronized Optional<Task> getNextTaskToProcess() {
    if (tasks.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(tasks.poll());
  }

  @Override
  public synchronized int numberOfTasksInTheBacklog() {
    return tasks.size();
  }
}

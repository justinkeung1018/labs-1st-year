package socialnetwork;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import socialnetwork.domain.Board;
import socialnetwork.domain.Message;

public class CoarseSyncBoard implements Board {
  CoarseOrderedLinkedList<Message> messages;

  public CoarseSyncBoard() {
    messages = new CoarseOrderedLinkedList<>();
  }

  @Override
  public boolean addMessage(Message message) {
    return messages.offerLast(message);
  }

  @Override
  public boolean deleteMessage(Message message) {
    return messages.remove(message);
  }

  @Override
  public int size() {
    return messages.size();
  }

  @Override
  public List<Message> getBoardSnapshot() {
    List<Message> snapshot = new ArrayList<>();
    Iterator<Message> it = messages.descendingIterator();
    while (it.hasNext()) {
      snapshot.add(it.next());
    }
    return snapshot;
  }
}

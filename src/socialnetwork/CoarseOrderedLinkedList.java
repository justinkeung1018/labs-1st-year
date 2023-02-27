package socialnetwork;

import java.util.Iterator;

public class CoarseOrderedLinkedList<E> {
  private final LinkedNode<E> head;
  private final LinkedNode<E> tail;
  private int size;

  public CoarseOrderedLinkedList() {
    head = new LinkedNode<>(Integer.MIN_VALUE);
    tail = new LinkedNode<>(Integer.MAX_VALUE);
    head.setNext(tail);
    tail.setPrev(head);
    size = 0;
  }

  public boolean offerFirst(E item) {
    return offer(item, head);
  }

  public boolean offerLast(E item) {
    return offer(item, tail);
  }

  private synchronized boolean offer(E item, LinkedNode<E> start) {
    Position<E> position = find(item, start);
    if (position.next().item() == item) {
      // Item has already been added
      return false;
    }
    size++;
    LinkedNode<E> node = new LinkedNode<>(item);
    position.prev().setNext(node);
    node.setPrev(position.prev());
    node.setNext(position.next());
    position.next().setPrev(node);
    return true;
  }

  public synchronized boolean remove(E item) {
    Position<E> position = find(item);
    if (position.next().item() != item) {
      // Message does not exist
      return false;
    }
    size--;
    LinkedNode<E> toDelete = position.next();
    position.prev().setNext(toDelete.next());
    toDelete.next().setPrev(position.prev());
    return true;
  }

  public synchronized int size() {
    assert size >= 0;
    return size;
  }

  public synchronized boolean isEmpty() {
    return size == 0;
  }

  public synchronized E poll() {
    if (isEmpty()) {
      return null;
    }
    size--;
    LinkedNode<E> toPoll = head.next();
    toPoll.next().setPrev(head);
    head.setNext(toPoll.next());
    return toPoll.item();
  }

  public Iterator<E> ascendingIterator() {
    return new Iterator<E>() {
      LinkedNode<E> curr = head;

      @Override
      public boolean hasNext() {
        return curr.next() != tail;
      }

      @Override
      public E next() {
        curr = curr.next();
        return curr.item();
      }
    };
  }

  public Iterator<E> descendingIterator() {
    return new Iterator<E>() {
      LinkedNode<E> curr = tail;

      @Override
      public boolean hasNext() {
        return curr.prev() != head;
      }

      @Override
      public E next() {
        curr = curr.prev();
        return curr.item();
      }
    };
  }

  private Position<E> find(E item) {
    return find(item, head);
  }

  private Position<E> find(E item, LinkedNode<E> start) {
    LinkedNode<E> prev = start;
    LinkedNode<E> next = start;
    assert start == head || start == tail;
    if (start == head) {
      do {
        prev = next;
        next = next.next();
      } while (next.hashCode() < item.hashCode());
    } else {
      do {
        next = prev;
        prev = prev.prev();
      } while (prev.hashCode() >= item.hashCode());
    }
    return new Position<>(prev, next);
  }
}

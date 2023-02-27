package socialnetwork;

public class LinkedNode<E> {
  private final E item;
  private LinkedNode<E> prev;
  private LinkedNode<E> next;
  private final int id;

  public LinkedNode(int id) {
    this.item = null;
    this.prev = null;
    this.next = null;
    this.id = id;
  }

  public LinkedNode(E item) {
    this.item = item;
    this.prev = null;
    this.next = null;
    this.id = item.hashCode();
  }

  public E item() {
    return item;
  }

  public LinkedNode<E> prev() {
    return prev;
  }

  public LinkedNode<E> next() {
    return next;
  }

  public void setPrev(LinkedNode<E> prev) {
    this.prev = prev;
  }

  public void setNext(LinkedNode<E> next) {
    this.next = next;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    assert item != null;
    return item.equals(((LinkedNode<?>) o).item());
  }
}

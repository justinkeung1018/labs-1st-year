package redblacktree;

public final class Node<K extends Comparable<? super K>, V> {

  private Node<K, V> left;
  private Node<K, V> right;
  private Node<K, V> parent;

  private Colour colour;

  private K key;
  private V value;

  public Node(K key, V value, Colour colour) {
    if (key == null) {
      throw new IllegalArgumentException("key cannot be null!");
    }

    this.key = key;
    this.value = value;
    this.colour = colour;
  }

  /* Tree operations */

  public Node<K, V> getParent() {
    return this.parent;
  }

  private void setParent(Node<K, V> node) {
    this.parent = node;
  }

  public void setRight(Node<K, V> node) {
    if (right != null && right.getParent() == this) {
      right.setParent(null);
    }
    this.right = node;
    if (node != null) {
      node.setParent(this);
    }
  }

  public void setLeft(Node<K, V> node) {
    if (left != null && left.getParent() == this) {
      left.setParent(null);
    }
    this.left = node;
    if (node != null) {
      node.setParent(this);
    }
  }

  public boolean isRoot() {
    return parent == null;
  }

  public boolean isLeftChild() {
    return parent != null && parent.getLeft() == this;
  }

  public boolean isRightChild() {
    return parent != null && parent.getRight() == this;
  }

  public Node<K, V> getLeft() {
    return this.left;
  }

  public Node<K, V> getRight() {
    return this.right;
  }

  public Node<K, V> getGrandparent() {
    return this.parent != null ? this.parent.parent : null;
  }

  public Node<K, V> getUncle() {
    Node<K, V> grandparent = getGrandparent();
    return grandparent != null
        ? (grandparent.left == this.parent ? grandparent.right : grandparent.left)
        : null;
  }

  public Node<K, V> rotateRight() {
    Node<K, V> oldLeft = left;
    left = oldLeft.right;
    if (left != null) {
      left.parent = this;
    }
    reparent(oldLeft);
    oldLeft.right = this;
    parent = oldLeft;
    return oldLeft;
  }

  public Node<K, V> rotateLeft() {
    Node<K, V> oldRight = right;
    right = oldRight.left;
    if (right != null) {
      right.parent = this;
    }
    reparent(oldRight);
    oldRight.left = this;
    parent = oldRight;
    return oldRight;
  }

  private void reparent(Node<K, V> replacement) {
    if (parent == null) {
      replacement.parent = null;
      return;
    }
    if (isLeftChild()) {
      parent.left = replacement;
    } else {
      parent.right = replacement;
    }
    replacement.parent = parent;
  }

  /* Colour operations */

  public boolean isBlack() {
    return colour == Colour.BLACK;
  }

  public boolean isRed() {
    return colour == Colour.RED;
  }

  public void setRed() {
    colour = Colour.RED;
  }

  public void setBlack() {
    colour = Colour.BLACK;
  }

  /* Key value operations */

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }

  public String toString() {
    return "{ " + colour + ": " + left + " [" + key + ", " + value + "] " + right + " }";
  }
}

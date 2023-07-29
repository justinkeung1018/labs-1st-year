package redblacktree;

import java.util.NoSuchElementException;

public class RedBlackTree<K extends Comparable<? super K>, V> {

  Node<K, V> root;

  public RedBlackTree() {
    this.root = null;
  }

  RedBlackTree(Node<K, V> root) {
    this.root = root;
  }

  public void put(K key, V value) {
    assert isValid();

    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);

    Node<K, V> parent = pair.getX();
    Node<K, V> current = pair.getY();

    if (current != null) { // found an existing key
      current.setValue(value);
      return;
    }

    if (parent == null) { // empty tree
      root = new Node<K, V>(key, value, Colour.BLACK);
      return;
    }
    /* create a new key */
    int comparison = key.compareTo(parent.getKey());
    Node<K, V> newNode = new Node<K, V>(key, value, Colour.RED);
    if (comparison < 0) {
      parent.setLeft(newNode);
    } else {
      assert comparison > 0;
      parent.setRight(newNode);
    }

    insertCaseOne(newNode);
  }

  private void insertCaseOne(Node<K, V> current) {
    if (current.getParent() == null) {
      current.setBlack();
      root = current;
      return;
    }
    insertCaseTwo(current);
  }

  private void insertCaseTwo(Node<K, V> current) {
    if (current.getParent().isBlack()) {
      return;
    }
    insertCaseThree(current);
  }

  private void insertCaseThree(Node<K, V> current) {
    assert current.getGrandparent() != null && current.getGrandparent().isBlack();
    if (current.getUncle() != null && current.getUncle().isRed()) {
      current.getUncle().setBlack();
      current.getParent().setBlack();
      current.getGrandparent().setRed();
      insertCaseOne(current.getGrandparent());
      return;
    }
    insertCaseFour(current);
  }

  private void insertCaseFour(Node<K, V> current) {
    assert current.getGrandparent() != null;
    Node<K, V> parent = current.getParent();
    if (parent.isLeftChild() && current.isRightChild()) {
      parent.rotateLeft();
      insertCaseFive(parent); // current.getParent() may not be equal to parent after rotation
    } else if (parent.isRightChild() && current.isLeftChild()) {
      parent.rotateRight();
      insertCaseFive(parent); // current.getParent() may not be equal to parent after rotation
    } else {
      insertCaseFive(current);
    }
  }

  private void insertCaseFive(Node<K, V> current) {
    assert current.getGrandparent() != null;
    current.getParent().setBlack();
    current.getGrandparent().setRed();
    Node<K, V> grandparent;
    if (current.isLeftChild()) {
      grandparent = current.getGrandparent().rotateRight();
    } else {
      grandparent = current.getGrandparent().rotateLeft();
    }
    if (grandparent.isRoot()) {
      root = grandparent;
    }
  }

  private boolean isValid() {
    return isValid(root);
  }

  private boolean isValid(Node<K, V> node) {
    if (node == null) {
      return true;
    }
    if (node.isRed() && (node.getParent() == null || node.getParent().isRed())) {
      return false;
    }
    if (node.getLeft() != null && node.getLeft().getParent() != node) {
      return false;
    }
    if (node.getRight() != null && node.getRight().getParent() != node) {
      return false;
    }
    if (countBlackNodesToLeaf(node) == -1) {
      return false; // Terribly inefficient as we call this at every node
    }
    return isValid(node.getLeft()) && isValid(node.getRight());
  }

  /**
   * If every path contains the same number of black nodes,
   * then return the number of black nodes on every path.
   * Otherwise, return -1.
   * @param node The root node of the current subtree.
   * @return The number of black nodes on every path to a leaf, or -1.
   */
  private int countBlackNodesToLeaf(Node<K, V> node) {
    if (node == null) {
      return 1; // Leaf nodes are black
    }
    int leftBlackNodes = countBlackNodesToLeaf(node.getLeft());
    int rightBlackNodes = countBlackNodesToLeaf(node.getRight());
    if (leftBlackNodes != rightBlackNodes) {
      return -1;
    }
    return (node.isBlack() ? 1 : 0) + leftBlackNodes;
  }

  private Tuple<Node<K, V>, Node<K, V>> findNode(K key) {
    assert isValid();
    Node<K, V> current = root;
    Node<K, V> parent = null;

    while (current != null) {
      parent = current;

      int comparison = key.compareTo(current.getKey());
      if (comparison < 0) {
        current = current.getLeft();
      } else if (comparison == 0) {
        break;
      } else {
        assert comparison > 0;
        current = current.getRight();
      }
    }
    return new Tuple<Node<K, V>, Node<K, V>>(parent, current);
  }

  public boolean contains(K key) {
    assert isValid();
    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);
    return pair.getY() != null;
  }

  public V get(K key) {
    assert isValid();
    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);
    Node<K, V> current = pair.getY();
    if (current == null) {
      throw new NoSuchElementException();
    }
    return current.getValue();
  }

  public void clear() {
    this.root = null;
  }

  public String toString() {
    return "RBT " + root + " ";
  }
}

package redblacktree;

import java.util.ArrayDeque;
import java.util.Deque;
import org.junit.ComparisonFailure;

public class NodeHelper {

  public static Node<Integer, String> redNode(Integer key) {
    return redNode(key, empty(), empty());
  }

  public static Node<Integer, String> redNode(
      Integer key, Node<Integer, String> left, Node<Integer, String> right) {
    Node<Integer, String> out = new Node<Integer, String>(key, key.toString(), Colour.RED);
    out.setLeft(left);
    out.setRight(right);
    return out;
  }

  public static Node<Integer, String> blackNode(Integer key) {
    return blackNode(key, empty(), empty());
  }

  public static Node<Integer, String> blackNode(
      Integer key, Node<Integer, String> left, Node<Integer, String> right) {
    Node<Integer, String> out = new Node<Integer, String>(key, key.toString(), Colour.BLACK);
    out.setLeft(left);
    out.setRight(right);
    return out;
  }

  public static Node<Integer, String> empty() {
    return null;
  }

  public static boolean nodeEquals(Node<Integer, String> a, Node<Integer, String> b) {

    Deque<Tuple<Node<Integer, String>, Node<Integer, String>>> nodesToVisit =
        new ArrayDeque<Tuple<Node<Integer, String>, Node<Integer, String>>>();

    nodesToVisit.addLast(new Tuple<Node<Integer, String>, Node<Integer, String>>(a, b));

    while (!nodesToVisit.isEmpty()) {

      Tuple<Node<Integer, String>, Node<Integer, String>> visit = nodesToVisit.removeFirst();

      a = visit.getX();
      b = visit.getY();

      if (a == b) {
        continue;
      }

      if (a == null || b == null) {
        return false;
      }

      if (a.isBlack() != b.isBlack()) {
        return false;
      }

      if (!(a.getKey().equals(b.getKey()))) {
        return false;
      }

      if (!(a.getValue().equals(b.getValue()))) {
        return false;
      }

      nodesToVisit.add(
          new Tuple<Node<Integer, String>, Node<Integer, String>>(a.getLeft(), b.getLeft()));
      nodesToVisit.add(
          new Tuple<Node<Integer, String>, Node<Integer, String>>(a.getRight(), b.getRight()));
    }

    return true;
  }

  static void checkTrees(RedBlackTree<Integer, String> tree, Node<Integer, String> expected) {
    if (!nodeEquals(tree.root, expected)) {
      throw new ComparisonFailure("Different trees", expected.toString(), tree.root.toString());
    }
  }
}

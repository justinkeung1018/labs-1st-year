package redblacktree;

import org.junit.Assert;
import org.junit.Test;

public class Contains {

  @Test
  public void testContainsSmall() {
    containsCheck(new Integer[] {1});
  }

  @Test
  public void testContainsLarge() {
    containsCheck(new Integer[] {1, 3, 5, 7, 9, 10, 8, 6, 4, 2, 0});
  }

  @Test
  public void testContainsFalse() {
    RedBlackTree<Integer, String> tree = new RedBlackTree<Integer, String>();
    Assert.assertFalse(tree.contains(9));
  }

  private void containsCheck(Integer[] keys) {
    RedBlackTree<Integer, String> tree = new RedBlackTree<Integer, String>();
    for (Integer k : keys) {
      tree.put(k, k.toString());
    }

    for (Integer k : keys) {
      Assert.assertTrue(tree.contains(k));
    }
  }
}

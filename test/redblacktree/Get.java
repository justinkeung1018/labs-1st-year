package redblacktree;

import org.junit.Assert;
import org.junit.Test;

public class Get {

  @Test
  public void testLookupSmall() {
    lookupCheck(new Integer[] {1});
  }

  @Test
  public void testLookupLarge() {
    lookupCheck(new Integer[] {1, 3, 5, 7, 9, 10, 8, 6, 4, 2, 0});
  }

  @Test
  public void testLookupFalse() {
    RedBlackTree<Integer, String> tree = new RedBlackTree<Integer, String>();
    Assert.assertFalse(tree.contains(9));
  }

  private void lookupCheck(Integer[] keys) {
    RedBlackTree<Integer, String> tree = new RedBlackTree<Integer, String>();
    for (Integer k : keys) {
      tree.put(k, k.toString());
    }

    for (Integer k : keys) {
      Assert.assertEquals(k.toString(), tree.get(k));
    }
  }
}

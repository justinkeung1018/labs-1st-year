package advancedstreams;

public class IsPalindrome {

  public static boolean isPalindrome(String string) {
    int l = 0;
    int r = string.length() - 1;
    while (r > l) {
      if (string.charAt(l) != string.charAt(r)) {
        return false;
      }
      l++;
      r--;
    }
    return true;
  }
}

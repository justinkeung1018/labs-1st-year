package rectangles;

public class Point {
  private final int x;
  private final int y;

  public Point(int x, int y) {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Coordinates must be non-negative.");
    }
    this.x = x;
    this.y = y;
  }

  public Point() {
    this.x = 0;
    this.y = 0;
  }

  public Point(int x) {
    this.x = x;
    this.y = 0;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Point setX(int newX) {
    return new Point(newX, y);
  }

  public Point setY(int newY) {
    return new Point(x, newY);
  }

  public boolean isLeftOf(Point other) {
    return x < other.getX();
  }

  public boolean isRightOf(Point other) {
    return x > other.getX();
  }

  public boolean isAbove(Point other) {
    return y < other.getY();
  }

  public boolean isBelow(Point other) {
    return y > other.getY();
  }

  public Point add(Point vector) {
    return new Point(x + vector.getX(), y + vector.getY());
  }
}

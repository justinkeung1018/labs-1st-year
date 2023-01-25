package rectangles;

public class Point {
  private final int xCoordinate;
  private final int yCoordinate;

  public Point(int x, int y) {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Coordinates must be non-negative.");
    }
    this.xCoordinate = x;
    this.yCoordinate = y;
  }

  public Point() {
    this.xCoordinate = 0;
    this.yCoordinate = 0;
  }

  public Point(int x) {
    this.xCoordinate = x;
    this.yCoordinate = 0;
  }

  public int getX() {
    return xCoordinate;
  }

  public int getY() {
    return yCoordinate;
  }

  public Point setX(int newX) {
    return new Point(newX, yCoordinate);
  }

  public Point setY(int newY) {
    return new Point(xCoordinate, newY);
  }

  public boolean isLeftOf(Point other) {
    return xCoordinate < other.getX();
  }

  public boolean isRightOf(Point other) {
    return xCoordinate > other.getX();
  }

  public boolean isAbove(Point other) {
    return yCoordinate < other.getY();
  }

  public boolean isBelow(Point other) {
    return yCoordinate > other.getY();
  }

  public Point add(Point vector) {
    return new Point(xCoordinate + vector.getX(), yCoordinate + vector.getY());
  }
}

package rectangles;

import java.util.Optional;

public class Rectangle {
  private final Point topLeft;
  private final Point bottomRight;

  public Rectangle(Point topLeft, int width, int height) {
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Width and height must be non-negative.");
    }
    this.topLeft = topLeft;
    this.bottomRight = new Point(topLeft.getX() + width, topLeft.getY() + height);
  }

  public Rectangle(Point point1, Point point2) {
    int topLeftX = Math.min(point1.getX(), point2.getX());
    int topLeftY = Math.min(point1.getY(), point2.getY());
    int bottomRightX = Math.max(point1.getX(), point2.getX());
    int bottomRightY = Math.max(point1.getY(), point2.getY());
    this.topLeft = new Point(topLeftX, topLeftY);
    this.bottomRight = new Point(bottomRightX, bottomRightY);
  }

  public Rectangle(int width, int height) {
    this(new Point(0, 0), width, height);
  }

  public int getWidth() {
    return bottomRight.getX() - topLeft.getX();
  }

  public int getHeight() {
    return bottomRight.getY() - topLeft.getY();
  }

  public Rectangle setWidth(int newWidth) {
    return new Rectangle(topLeft, newWidth, getHeight());
  }

  public Rectangle setHeight(int newHeight) {
    return new Rectangle(topLeft, getWidth(), newHeight);
  }

  public Point getTopLeft() {
    return topLeft;
  }

  public Point getTopRight() {
    return new Point(bottomRight.getX(), topLeft.getY());
  }

  public Point getBottomLeft() {
    return new Point(topLeft.getX(), bottomRight.getY());
  }

  public Point getBottomRight() {
    return bottomRight;
  }

  public int area() {
    return getWidth() * getHeight();
  }

  public boolean intersects(Rectangle other) {
    return getTopLeft().getX() <= other.getTopRight().getX()
        && getTopRight().getX() >= other.getTopLeft().getX()
        && getTopLeft().getY() <= other.getBottomLeft().getY()
        && getBottomLeft().getY() >= other.getTopLeft().getY();
  }

  public Optional<Rectangle> intersection(Rectangle other) {
    if (!intersects(other)) {
      return Optional.empty();
    }
    Point topLeft = new Point(Math.max(getTopLeft().getX(), other.getTopLeft().getX()),
        Math.max(getTopLeft().getY(), other.getTopLeft().getY()));
    Point bottomRight = new Point(Math.min(getBottomRight().getX(), other.getBottomRight().getX()),
        Math.min(getBottomRight().getY(), other.getBottomRight().getY()));
    return Optional.of(new Rectangle(topLeft, bottomRight));
  }
}

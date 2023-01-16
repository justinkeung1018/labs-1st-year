package picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that encapsulates and provides a simplified interface for manipulating an image. The
 * internal representation of the image is based on the RGB direct colour model.
 */
public class Picture {

  /** The internal image representation of this picture. */
  private BufferedImage image;

  /** Construct a new (blank) Picture object with the specified width and height. */
  public Picture(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  /** Construct a new Picture from the image data in the specified file. */
  public Picture(String filepath) {
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Blends the list of pictures such that they appear layered on top of each other. If the pictures
   * are of different sizes, they will be aligned to the top left corner and the resultant blended
   * picture will have the minimum width and height of all the pictures.
   *
   * @param pictures The list of pictures to be blended.
   * @return The blended picture.
   */
  public static Picture blend(List<Picture> pictures) {
    int numPictures = pictures.size();
    if (numPictures == 0) {
      throw new IllegalArgumentException("At least one picture must be passed in.");
    }
    int minWidth = pictures.stream().mapToInt(Picture::getWidth).min().orElse(0);
    int minHeight = pictures.stream().mapToInt(Picture::getHeight).min().orElse(0);
    Picture result = new Picture(minWidth, minHeight);
    for (int x = 0; x < minWidth; x++) {
      for (int y = 0; y < minHeight; y++) {
        int redTotal = 0;
        int greenTotal = 0;
        int blueTotal = 0;
        for (Picture picture : pictures) {
          Color rgb = picture.getPixel(x, y);
          redTotal += rgb.getRed();
          greenTotal += rgb.getGreen();
          blueTotal += rgb.getBlue();
        }
        int redAvg = redTotal / numPictures;
        int greenAvg = greenTotal / numPictures;
        int blueAvg = blueTotal / numPictures;
        result.setPixel(x, y, new Color(redAvg, greenAvg, blueAvg));
      }
    }
    return result;
  }

  /**
   * Test if the specified point lies within the boundaries of this picture.
   *
   * @param x the x co-ordinate of the point
   * @param y the y co-ordinate of the point
   * @return <tt>true</tt> if the point lies within the boundaries of the picture, <tt>false</tt>
   *     otherwise.
   */
  public boolean contains(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  /**
   * Returns true if this Picture is graphically identical to the other one.
   *
   * @param other The other picture to compare to.
   * @return true iff this Picture is graphically identical to other.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof Picture)) {
      return false;
    }

    Picture otherPic = (Picture) other;

    if (image == null || otherPic.image == null) {
      return image == otherPic.image;
    }
    if (image.getWidth() != otherPic.image.getWidth()
        || image.getHeight() != otherPic.image.getHeight()) {
      return false;
    }

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getRGB(i, j) != otherPic.image.getRGB(i, j)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Return the height of the <tt>Picture</tt>.
   *
   * @return the height of this <tt>Picture</tt>.
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Return the colour components (red, green, then blue) of the pixel-value located at (x,y).
   *
   * @param x x-coordinate of the pixel value to return
   * @param y y-coordinate of the pixel value to return
   * @return the RGB components of the pixel-value located at (x,y).
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *     the boundaries of this picture.
   */
  public Color getPixel(int x, int y) {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

  /**
   * Return the width of the <tt>Picture</tt>.
   *
   * @return the width of this <tt>Picture</tt>.
   */
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int hashCode() {
    if (image == null) {
      return -1;
    }
    int hashCode = 0;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        hashCode = 31 * hashCode + image.getRGB(i, j);
      }
    }
    return hashCode;
  }

  public void saveAs(String filepath) {
    try {
      ImageIO.write(image, "png", new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Update the pixel-value at the specified location.
   *
   * @param x the x-coordinate of the pixel to be updated
   * @param y the y-coordinate of the pixel to be updated
   * @param rgb the RGB components of the updated pixel-value
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *     the boundaries of this picture.
   */
  public void setPixel(int x, int y, Color rgb) {

    image.setRGB(
        x,
        y,
        0xff000000
            | (((0xff & rgb.getRed()) << 16)
                | ((0xff & rgb.getGreen()) << 8)
                | (0xff & rgb.getBlue())));
  }

  /** Returns a String representation of the RGB components of the picture. */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        sb.append("(");
        sb.append(rgb.getRed());
        sb.append(",");
        sb.append(rgb.getGreen());
        sb.append(",");
        sb.append(rgb.getBlue());
        sb.append(")");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  /** Inverts the colour components of each pixel in the picture. */
  public void invert() {
    int maxIntensity = Color.getMaxIntensity();
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        Color rgb = getPixel(x, y);
        int red = rgb.getRed();
        int green = rgb.getGreen();
        int blue = rgb.getBlue();
        setPixel(x, y, new Color(maxIntensity - red, maxIntensity - green, maxIntensity - blue));
      }
    }
  }

  /** Turns the picture into grayscale. */
  public void grayscale() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        Color rgb = getPixel(x, y);
        int average = (rgb.getRed() + rgb.getGreen() + rgb.getBlue()) / 3;
        setPixel(x, y, new Color(average, average, average));
      }
    }
  }

  /** Rotates the picture by the specified number of degrees (90, 180, or 270). */
  public void rotate(int degrees) {
    if (degrees != 90 && degrees != 180 && degrees != 270) {
      throw new IllegalArgumentException("Number of degrees must be a non-zero multiple of 90.");
    }

    Picture result = this;
    int numRotations = degrees / 90;
    for (int rotation = 0; rotation < numRotations; rotation++) {
      Picture rotatedOnce = new Picture(result.getHeight(), result.getWidth());
      for (int x = 0; x < result.getWidth(); x++) {
        for (int y = 0; y < result.getHeight(); y++) {
          rotatedOnce.setPixel(rotatedOnce.getWidth() - y - 1, x, result.getPixel(x, y));
        }
      }
      result = rotatedOnce;
    }
    image = result.image;
  }

  /** Flips the picture horizontally. */
  public void flipHorizontal() {
    int midpointX = getWidth() / 2;
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < midpointX; x++) {
        int oppositeX = getWidth() - x - 1;
        Color oppositeRGB = getPixel(oppositeX, y);
        setPixel(oppositeX, y, getPixel(x, y));
        setPixel(x, y, oppositeRGB);
      }
    }
  }

  /** Flips the picture vertically. */
  public void flipVertical() {
    int midpointY = getHeight() / 2;
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < midpointY; y++) {
        int oppositeY = getHeight() - y - 1;
        Color oppositeRGB = getPixel(x, oppositeY);
        setPixel(x, oppositeY, getPixel(x, y));
        setPixel(x, y, oppositeRGB);
      }
    }
  }

  /** Blurs the picture. */
  public void blur() {
    Color[][] pixels = new Color[getWidth()][getHeight()];
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        pixels[x][y] = getPixel(x, y);
      }
    }
    int neighbourhoodWidth = 3;
    int neighbourhoodHeight = 3;
    int maxdx = neighbourhoodWidth / 2;
    int maxdy = neighbourhoodHeight / 2;
    for (int x = 1; x < getWidth() - 1; x++) {
      for (int y = 1; y < getHeight() - 1; y++) {
        List<Color> neighbourhood = new ArrayList<>();
        for (int dx = -maxdx; dx <= maxdx; dx++) {
          for (int dy = -maxdy; dy <= maxdy; dy++) {
            neighbourhood.add(pixels[x + dx][y + dy]);
          }
        }
        int numPixels = neighbourhood.size();
        int redAvg = neighbourhood.stream().mapToInt(Color::getRed).sum() / numPixels;
        int greenAvg = neighbourhood.stream().mapToInt(Color::getGreen).sum() / numPixels;
        int blueAvg = neighbourhood.stream().mapToInt(Color::getBlue).sum() / numPixels;
        setPixel(x, y, new Color(redAvg, greenAvg, blueAvg));
      }
    }
  }
}

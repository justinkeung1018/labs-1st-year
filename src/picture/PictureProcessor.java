package picture;

import java.util.ArrayList;
import java.util.List;

public class PictureProcessor {

  public static void main(String[] args) {
    switch (args[0]) {
      case "invert" -> {
        Picture picture = new Picture(args[1]);
        picture.invert().saveAs(args[2]);
      }
      case "grayscale" -> {
        Picture picture = new Picture(args[1]);
        picture.grayscale().saveAs(args[2]);
      }
      case "rotate" -> {
        int degrees = Integer.parseInt(args[1]);
        Picture picture = new Picture(args[2]);
        picture.rotate(degrees).saveAs(args[3]);
      }
      case "flip" -> {
        String flipDirection = args[1];
        Picture picture = new Picture(args[2]);
        String destination = args[3];
        if (flipDirection.equals("H")) {
          picture.flipHorizontal().saveAs(destination);
        } else if (flipDirection.equals("V")) {
          picture.flipVertical().saveAs(destination);
        }
      }
      case "blend" -> {
        int numArgs = args.length;
        List<Picture> pictures = new ArrayList<>();
        for (int i = 1; i < numArgs - 1; i++) {
          pictures.add(new Picture(args[i]));
        }
        Picture.blend(pictures).saveAs(args[numArgs - 1]);
      }
      case "blur" -> {
        Picture picture = new Picture(args[1]);
        picture.blur().saveAs(args[2]);
      }
    }
  }
}

package picture;

import java.util.ArrayList;
import java.util.List;

public class PictureProcessor {

  public static void main(String[] args) {
    switch (args[0]) {
      case "invert" -> {
        Picture picture = new Picture(args[1]);
        picture.invert();
        picture.saveAs(args[2]);
      }
      case "grayscale" -> {
        Picture picture = new Picture(args[1]);
        picture.grayscale();
        picture.saveAs(args[2]);
      }
      case "rotate" -> {
        int degrees = Integer.parseInt(args[1]);
        Picture picture = new Picture(args[2]);
        picture.rotate(degrees);
        picture.saveAs(args[3]);
      }
      case "flip" -> {
        String flipDirection = args[1];
        Picture picture = new Picture(args[2]);
        if (flipDirection.equals("H")) {
          picture.flipHorizontal();
        } else if (flipDirection.equals("V")) {
          picture.flipVertical();
        }
        picture.saveAs(args[3]);
      }
      case "blend" -> {
        int numArgs = args.length;
        List<Picture> pictures = new ArrayList<>();
        for (int i = 1; i < numArgs - 1; i++) {
          pictures.add(new Picture(args[i]));
        }
        Picture result = Picture.blend(pictures);
        result.saveAs(args[numArgs - 1]);
      }
      case "blur" -> {
        Picture picture = new Picture(args[1]);
        picture.blur();
        picture.saveAs(args[2]);
      }
    }
  }
}

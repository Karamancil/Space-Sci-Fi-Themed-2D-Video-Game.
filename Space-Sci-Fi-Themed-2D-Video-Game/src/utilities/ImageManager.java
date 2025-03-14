package utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    public final static String path = "images/";
    public final static String ext = ".png";

    public static Map<String, Image> images = new HashMap<String, Image>();

    public static Image getImage(String s) {
        return images.get(s);
    }

    public static Image loadImage(String fname) throws IOException {
        BufferedImage img = null;
        img = ImageIO.read(new File(path + fname + ext));
        images.put(fname, img);
        System.out.println("loaded image");
        return img;
    }

    public static Image loadImage(String imName, String fname) throws IOException {
        BufferedImage img = null;
        img = ImageIO.read(new File(path + fname + ext));
        images.put(imName, img);
        return img;
    }

    public static void loadImages(String[] fNames) throws IOException {
        for (String s : fNames)
            loadImage(s);
    }

    public static void loadImages(Iterable<String> fNames) throws IOException {
        for (String s : fNames)
            loadImage(s);
    }

}

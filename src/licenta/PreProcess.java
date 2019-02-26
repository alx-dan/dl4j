package licenta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PreProcess {

    public BufferedImage l, o, img;

    public BufferedImage luminance(BufferedImage original) {

        BufferedImage lumi = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                //GET THE RGB PIXELS
                int alpha = new Color(original.getRGB(i, j)).getAlpha(); //sRGB transparency 0-1 or 0-255
                int red = new Color(original.getRGB(i, j)).getRed();
                int green = new Color(original.getRGB(i, j)).getGreen();
                int blue = new Color(original.getRGB(i, j)).getBlue();

                int x = (int) (0.21 * red + 0.71 * green + 0.07 * blue); //converted to integer
                int newPixel = toRGB(alpha, x, x, x); // return back to original
                lumi.setRGB(i, j, newPixel); // write pixel
            }
        }
        return lumi;
    }

    // Convert R, G, B, Alpha to standard 8 bit (1 byte = 8 bits) (xxxx xxxY for << 8)
    private static int toRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }






    public PreProcess (BufferedImage img) {o = img;};
    public PreProcess (String path){
        File source = new File(path);
        try {
            o = ImageIO.read(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) {
        PreProcess alx = new PreProcess("original"+".jpg");
        l = alx.luminance(o);
    }
}
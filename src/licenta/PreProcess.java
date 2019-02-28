package licenta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class PreProcess {

    public BufferedImage orig;

    public BufferedImage togray(BufferedImage original) {
        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                Color c = new Color(original.getRGB(i, j)); //get grb pixels
                int red = (int)(c.getRed()*0.21);
                int green = (int)(c.getGreen()*0.72);
                int blue = (int)(c.getBlue()*0.07);
                Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue); // return to original
                image.setRGB(i, j, newColor.getRGB()); // write pixel
            }
        }
        return image;
    }








    public PreProcess (BufferedImage img) { orig = img;}
    public PreProcess (String path){
        File source = new File(path);
        try {
            orig = ImageIO.read(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) {
        PreProcess alx = new PreProcess("original"+".jpg");
        alx.togray(orig);
    }
}
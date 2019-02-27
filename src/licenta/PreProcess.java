package licenta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PreProcess {

    public BufferedImage l, o;
    public BufferedImage togray(BufferedImage original) {

        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                Color c = new Color(original.getRGB(i, j)); //get grb pixels
                int red = (int)(c.getRed()*0.21); //converted to integer
                int green = (int)(c.getGreen()*0.71);
                int blue = (int)(c.getBlue()*0.07);
                Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue); // return to original
                image.setRGB(i, j, newColor.getRGB()); // write pixel
            }
        }
        return image;
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
        l = alx.togray(o);
    }
}
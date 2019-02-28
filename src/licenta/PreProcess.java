package licenta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class PreProcess {

    public BufferedImage orig, gray, binar;

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

    public BufferedImage tobinary (BufferedImage original) {
        int pixel;
        int Treshold = Treshold(original);
        BufferedImage image = new BufferedImage (original.getWidth(), original.getHeight(), original.getType());
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                Color c = new Color (original.getRGB(i, j));
                int red = c.getRed();
                if (red > Treshold){
                    pixel = 255;
                }
                else {
                    pixel = 0;
                }
             Color newColor = new Color(pixel, pixel, pixel);
             image.setRGB(i, j, newColor.getRGB());
            }
        }
        return image;
    }

    private int Treshold (BufferedImage original){
        int total = original.getHeight() * original.getWidth();
        int[] histogram = imageHisto (original);
        float sum = 0;
        for (int i = 0; i < 256; i++)
            sum += i*histogram[i];
        float suma = 0, max =0;
        int b = 0, f , treshold = 0;
        for (int i = 0; i < 256; i++) {
            b += histogram[i];
            if (b == 0) continue;
            f = total - b;
            if (f == 0) break;
            suma += (float) (i*histogram[i]);
            float B = suma / b;
            float F = (sum - suma) / f;
            float btwn = (float) b * (float) f * (B-F)* (B-F);
            if (btwn > max){
                max = btwn;
                treshold = i;
            }
        }
        return treshold;
    }
    private int[] imageHisto(BufferedImage input) {
        int [] historgam = new int [256];
        for (int i = 0; i <historgam.length ; i++)
            historgam [i]=0;
        for (int i = 0; i <input.getWidth(); i++) {
            for (int j = 0; j <input.getHeight(); j++) {
                int red = new Color(input.getRGB(i,j)).getRed();
                historgam[red]++;
            }
        }
        return historgam;
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
        gray = alx.togray(orig);
        binar = alx.tobinary(gray);
    }
}
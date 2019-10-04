package licenta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.imgscalr.Scalr;

@SuppressWarnings("WeakerAccess")
public class Process {

    public class par{
        int f;
        int s;
        public par(int f,int s){
            this.f = f;
            this.s = s;
        }
    }


    public BufferedImage orig,gray,binar,img;
    public int counter = 1 ;
    private int nr = 1;
    private int id;
    private int []x1 = new int[2000]; // l t corner
    private int []x2 = new int[2000]; // r b corner
    private int []y1 = new int[2000]; // l t corner
    private int []y2 = new int[2000]; // r b corner
    private int [][]visit = new int [1000][1000]; //highest image in pixels
    private static final int squareSize = 28;
    private static double [][]o = new double[squareSize][squareSize];
    private int []dfsx = {0, 0, 1, -1, 1, 1, -1, -1};// moving x dir
    private int []dfsy = {1, -1, 0, 0, 1, -1, -1, 1};// moving y dir
    //private static ArrayList<BufferedImage> imDbg = new ArrayList<>();
    public ArrayList<ArrayList<Double> >out = new ArrayList<>();

//image pre-processing
//    ---------------------------------------------------------------------------------   //
//image pre-processing

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
                int x = c.getRed();
                if (x > Treshold){
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

// image segmentation
//    ---------------------------------------------------------------------------------   //
// image segmentation

    public void seg() {
          System.out.println("Height & Width: " + binar.getHeight() + " " + binar.getWidth());
            for(int i = 0; i < binar.getWidth(); i++){
            for(int j = 0; j < binar.getHeight(); j++){
                if(!visited(i, j)){
                    counter = nr;
                    System.out.println("box coordinates(pix): " + i + "/" + j);
                    System.out.println("you wrote " + counter + " number/s");
                    clr(id);
                    box(i, j, id);
                    fin(id);
                    print();
                    csv();
                    id++;
                    nr++;
                }
            }
        }
        System.out.println(x1[0] + " " + y1[0] + " " + x2[0] + " " + y2[0]);
    }

    private Boolean visited (int x, int y){
        //out of boundaries ?
        return x < 0 || y < 0 || y >= binar.getHeight() || x >= binar.getWidth() || visit[x][y] == 1 || !black(binar, x, y);
    }
    private Boolean black(BufferedImage input,int x, int y){
        int red = new Color(input.getRGB(x,y)).getRed();
        return red == 0; //0.0.0 is black
    }
    private void clr (int id){
        x1[id]=10000;
        y1[id]=10000;
        x2[id]=-10000;
        y2[id]=-10000;
        int m = Math.max(binar.getWidth(),binar.getHeight())*3;
        img = new BufferedImage(m,m,binar.getType());
        clrImg(img);
        for (int i = 0; i < squareSize; i++)
            for (int j = 0; j < squareSize; j++){
                o[i][j]=0;
            }
    }
    private void clrImg (BufferedImage input){
        Graphics2D graphic = input.createGraphics();
        graphic.setPaint(new Color(255,255,255)); //white fill
        graphic.fillRect(0,0,input.getWidth(),input.getHeight());
    }
    private void box (int x, int y, int id){ //breadth first search
        int safe = Math.max(binar.getWidth(),binar.getHeight()); //max dimensions
        Queue<par> q =  new LinkedList<>();//holding elements prior to processing in a doubly-linked list
        q.add(new par(x,y));
        while (!q.isEmpty()){
            int X = q.peek().f; //the object at the top of this stack
            int Y = q.peek().s;
            q.remove();
            if (visited(X,Y))
                continue;
            visit [X][Y]=1;

            x1[id] = Math.min(X+safe, x1[id]); //t l
            y1[id] = Math.min(Y+safe, y1[id]);
            x2[id] = Math.max(X+safe, x2[id]); //b r
            y2[id] = Math.max(Y+safe, y2[id]);
            if (black(binar,X,Y)){
                sBlack(img,X+safe,Y+safe);
                bld(img,X+safe, Y+safe);
            }
            for (int i = 0; i < 4; i++)
                for (int j = 1; j <=1 ; j++)
                    q.add(new par(X+dfsx[i]*j,Y+dfsy[i]*j));//throught all 4 dir
        }
    }
    private void sBlack (BufferedImage input, int x, int y){
        if(x<0||y<0||x>=input.getWidth()||y>=input.getHeight())
            return;
        Color c = new Color(0,0,0);
        input.setRGB(x,y,c.getRGB()); //black pixel
    }
    private void bld (BufferedImage input, int x, int y){
        for (int i = 0; i < 8; i++)
            for (int j = 0; j <=2 ; j++) //it was 2 px
                sBlack(input,x+dfsx[i]*j,y+dfsy[i]*j);   //set to black 2 pixels in all directions
    }

    private void fin (int id){
        int m = Math.max(x2[id]-x1[id],y2[id]-y1[id]);
        img = Scalr.crop(img, Math.max(x1[id],0),Math.max(y1[id],0),m,m);
        img = Scalr.resize(img,Scalr.Method.ULTRA_QUALITY,20,20,Scalr.OP_ANTIALIAS,Scalr.OP_BRIGHTER);
        try {
            ImageIO.write(img, "jpg", new File("./images/" + "brut" + id + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        array();
        centr();
        img = Scalr.pad(img,4,Color.WHITE);
        arrayLst();
        try {
            ImageIO.write(img, "jpg", new File("./images/" + id + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //imDbg.add(img);
    }
    private void array(){
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int red = new Color(img.getRGB(i,j)).getRed();
                if (red <=180)
                    o[j][i] = 1;
            }
        }
    }
    private void arrayLst(){
        clrImg(img);
        for (int i = 0; i < squareSize; i++)
            for (int j = 0; j < squareSize; j++)
                if (o[j][i]==1)
                    sBlack(img,i,j);
        ArrayList<Double> pixel = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Integer v;
                if(img.getRGB(j,i)==-1)
                    v = 0;
                else
                    v = 1;
                pixel.add(v.doubleValue());
            }
        }
        out.add(pixel);
    }
    private void centr(){
        int left = 50, right = -50, up = 50, down = -50, shiftX, shiftY;
        for(int i = 0; i < squareSize; i++)
            for(int j = 0; j < squareSize; j++)
                if(o[i][j] == 1){
                    up = Math.min(up, i);
                    down = Math.max(down, i);
                    right = Math.max(right, j);
                    left = Math.min(left, j);
                }
        shiftX = (left + squareSize - 1 - right) / 2;
        shiftY = (up + squareSize - 1 - down) / 2;
        double [][]temp = new double[squareSize][squareSize];
        for(int i = shiftY; i < squareSize; i++)
            System.arraycopy(o[i - shiftY], 0, temp[i], shiftX, squareSize - shiftX);
        for(int i = 0; i < squareSize; i++)
            System.arraycopy(temp[i], 0, o[i], 0, squareSize);
    }
    private void print(){
        for (int i = 0; i < squareSize; i++) {
            for (int j = 0; j < squareSize; j++)
                if(o[i][j] > 0.5)
                    System.out.print(1);
                else
                    System.out.print(0);
            System.out.println();
        }
        System.out.println("=======================================");
    }

    private void csv(){
        try(PrintWriter writter = new PrintWriter(new File("./segm/test"+id+".csv"))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < squareSize; i++) {
                for (int j = 0; j < squareSize; j++)
                    if (o[i][j] > 0.5)
                        sb.append("1,");
                    else
                        sb.append("0,");
            }
            writter.write(sb.substring(0,sb.length() - 1 ));
        }catch (FileNotFoundException e){System.out.println(e.getMessage());}
    }

    public Process(BufferedImage img) { orig = img;}
    public Process(String path){
        File source = new File(path);
        try {
            orig = ImageIO.read(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) {

        Process alx = new Process("original"+".jpg");
        gray = alx.togray(orig);
        binar = alx.tobinary(gray);
        alx.seg();

    }
}
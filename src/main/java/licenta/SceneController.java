package licenta;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;


public class SceneController{

    @FXML
    public Label nr_label;

    @FXML
    public TextArea text_area;

    @FXML
    private ImageView myImageView,myImageView1,myImageView2,processed1,processed2,processed3,processed11,processed21,processed31;
    private BufferedImage img,img1,img2,img3,img11,img21,img31;

    //@Override
    //public void initialize(URL location, ResourceBundle resources) {
    //}

    @FXML //for each fx:id=
    private void loadimg() {
        //previous files and text area cleanup

        text_area.setText("");

        File dirImg = new File("./images/");
        for (File del: dirImg.listFiles())
            if (!del.isDirectory())
                del.delete();

        File dirSeg = new File("./segm/");
        for (File del: dirSeg.listFiles())
            if (!del.isDirectory())
                del.delete();

        File dirOut = new File("./str/");
        for (File del: dirOut.listFiles())
            if (!del.isDirectory())
                del.delete();

        try (PrintWriter out = new PrintWriter("./str/out.txt")) {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileChooser file = new FileChooser();
        file.setInitialDirectory(new File("D:\\personale\\fac\\lic"));
        File file1 = file.showOpenDialog(null);
        try {
            img = ImageIO.read(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image image  = SwingFXUtils.toFXImage(img,null);
        myImageView.setImage(image);
    }

    @FXML
    private void processimg(){

        Process p = new Process(img);

        p.gray = p.togray(p.orig);
        Image imageG = SwingFXUtils.toFXImage(p.gray,null);
        myImageView1.setImage(imageG);

        p.binar = p.tobinary(p.gray);
        Image imageB = SwingFXUtils.toFXImage(p.binar, null);
        myImageView2.setImage(imageB);

        p.seg();

        File ferr = new File("./err/err.jpg");

        File f1 = new File("./images/brut0.jpg");
        try {
            img1 = ImageIO.read(f1);
        } catch (IOException e) {
            try {
                img1 = ImageIO.read(ferr);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        Image image1  = SwingFXUtils.toFXImage(img1,null);
        processed1.setImage(image1);

        File f2 = new File("./images/brut1.jpg");
        try {
            img2 = ImageIO.read(f2);
        } catch (IOException e) {
            try {
                img2 = ImageIO.read(ferr);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        Image image2  = SwingFXUtils.toFXImage(img2,null);
        processed2.setImage(image2);

        File f3 = new File("./images/brut2.jpg");
        try {
            img3 = ImageIO.read(f3);
        } catch (IOException e) {
            try {
                img3 = ImageIO.read(ferr);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        Image image3  = SwingFXUtils.toFXImage(img3,null);
        processed3.setImage(image3);

        File f11 = new File("./images/0.jpg");
        try {
            img11 = ImageIO.read(f11);
        } catch (IOException e) {
            try {
                img11 = ImageIO.read(ferr);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        Image image11  = SwingFXUtils.toFXImage(img11,null);
        processed11.setImage(image11);

        File f21 = new File("./images/1.jpg");
        try {
            img21 = ImageIO.read(f21);
        } catch (IOException e) {
            try {
                img21 = ImageIO.read(ferr);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        Image image21  = SwingFXUtils.toFXImage(img21,null);
        processed21.setImage(image21);

        File f31 = new File("./images/2.jpg");
        try {
            img31 = ImageIO.read(f31);
        } catch (IOException e) {
            try {
                img31 = ImageIO.read(ferr);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        Image image31  = SwingFXUtils.toFXImage(img31,null);
        processed31.setImage(image31);

        //boxes counter
        String string = Integer.toString(p.counter);
        nr_label.setText(string);

        //textarea fill from file
        Scanner s = null;
        try {
            s = new Scanner(new File("./str/out.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (s.hasNextLine()) {
            text_area.appendText(s.next());
        }
    }
    }


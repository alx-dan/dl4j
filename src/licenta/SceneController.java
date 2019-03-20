package licenta;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class SceneController{

    @FXML
    private ImageView myImageView, myImageView1, myImageView2;
    private BufferedImage img;

    //@Override
    //public void initialize(URL location, ResourceBundle resources) {
    //}

    @FXML //for each fx:id=
    private void loadimg() {
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
    }
    }


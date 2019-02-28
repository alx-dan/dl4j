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
    private ImageView myImageView,myImageView1;
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
        PreProcess p = new PreProcess(img);
        Image imageL  = SwingFXUtils.toFXImage(p.togray(p.orig),null);
        myImageView1.setImage(imageL);

    }
    }


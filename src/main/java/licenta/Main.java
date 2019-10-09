package licenta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("SceneBuild.fxml"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("SceneBuild.fxml"));
        primaryStage.setTitle("A.Craciunescu");
        primaryStage.setScene(new Scene(root, 1500, 900));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

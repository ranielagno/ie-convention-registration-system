package registration.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Closing implements Initializable {

    @FXML
    private ImageView registration_complete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(3000),
                e-> {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/registration/views/xml/home.fxml"));
                    loader.setController(new Home());


                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Stage stage = (Stage) registration_complete.getScene().getWindow();
                    Scene scene = new Scene(root, 1200, 700);
                    stage.setScene(scene);
                    stage.show();
                }
        ));
        timeline.play();
    }
}

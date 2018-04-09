package registration.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class Home {

    @FXML
    private JFXButton register;

    public void registerNow() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/registration/views/xml/registration.fxml"));
        loader.setController(new Registration());

        Parent root = null;
        try {
            root = loader.load();
            Stage stage = (Stage) register.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }


    }
}

package registration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import registration.controllers.Home;
import registration.network.ConnectionManager;
import registration.network.NetworkConfig;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        ConnectionManager connectionManager = new ConnectionManager();

        boolean isDatabaseConnected = connectionManager.connectToDatabase(
                NetworkConfig.URL,
                NetworkConfig.USERNAME,
                NetworkConfig.PASSWORD,
                NetworkConfig.DATABASE_NAME);

        if (isDatabaseConnected) {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/xml/home.fxml"));
            loader.setController(new Home());

            Parent root = loader.load();

            primaryStage.setOnCloseRequest(error -> System.exit(0));

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            primaryStage.setTitle("IE Registration System");
            primaryStage.setScene(new Scene(root, 1200, 700));
            primaryStage.show();

        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}

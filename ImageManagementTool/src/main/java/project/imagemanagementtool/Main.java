package project.imagemanagementtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-controller.fxml"));
        Scene scene = new Scene(root,600,400);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("pikachu.png")));
        stage.setTitle("Image Management Tool");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
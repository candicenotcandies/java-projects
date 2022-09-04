package project.imagemanagementtool;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PropertyController {
    @FXML
    public TextField nameScene;
    @FXML
    public TextField heightScene;
    @FXML
    public TextField widthScene;
    @FXML
    public TextField locateScene;
    @FXML
    //Show image property in second scene
    public void showInformation(String name, String height, String width, String locate) {
        nameScene.setText(name);
        heightScene.setText(height);
        widthScene.setText(width);
        locateScene.setText(locate);
    }
}
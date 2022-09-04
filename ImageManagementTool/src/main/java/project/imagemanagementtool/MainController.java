package project.imagemanagementtool;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainController {

    @FXML
    private ImageView imageView;
    @FXML
    private ImageView imageResult;
    @FXML
    private Button uploadFile;
    @FXML
    private File file;
    @FXML
    private Image imageChose;
    @FXML
    private Image imageModified;
    @FXML
    private boolean newImage;
    @FXML
    private Stage s;
    @FXML
    private Scene childrenScene;
    @FXML
    private Parent children;
    @FXML
    private String name;
    @FXML
    private String locate;
    @FXML
    private double width;
    @FXML
    private double height;

    public void switchToScene2(ActionEvent e) throws IOException {
        //create a new stage and a new scene, and call the function in the new scene to transfer image property from
        // main-controller to property-controller and make two controllers communicate with each other.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("property-window.fxml"));
        children = loader.load();
        loader.getController();
        PropertyController propertyController = loader.getController();
        propertyController.showInformation(name, Double.toString(height), Double.toString(width),locate);
        s = new Stage();
        s.setTitle("Image Property");
        childrenScene = new Scene(children);
        s.setScene(childrenScene);
        s.show();
    }

    @FXML
    public void UploadFile(ActionEvent e) throws IOException {
        //Function: Upload File
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open and Choose Image");
        Stage stage = (Stage) uploadFile.getScene().getWindow();
        //Only file with the extension of jpg/jpeg/png with be allowed
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.jpeg"));
        file = fileChooser.showOpenDialog(stage);
        imageChose = new Image(file.toURI().toString());
        imageView.setImage(imageChose);
        //get originall image property
        width = imageChose.getWidth();
        height = imageChose.getHeight();
        name = file.getName();
        locate = file.getAbsolutePath();
        //Mark this is a new Image
        newImage=true;
        //Action will be recorded to "log.txt".
        WriteToFile("Upload File");
    }



    @FXML
    private void Fade(ActionEvent e) throws Exception {
        //Function: fade a picture using color adjust.
        if (this.file !=null) {
            ColorAdjust fade = new ColorAdjust();
            fade.setSaturation(-1.0);
            imageResult.setEffect(fade);
            imageResult.setCache(true);
            imageResult.setCacheHint(CacheHint.SPEED);
            //If there is nothing on imageResult or there is a new upload, we will directly use the original file.
            if(imageModified==null || newImage==true) {
            imageModified = new Image(file.toURI().toString());
            } else {
                //if the image has been edited (e.g. downsized), we will use the image of imageResult.
                imageModified = imageResult.snapshot(null, null);
            }
            imageResult.setImage(imageModified);
            WriteToFile("Fade");

        }else{
            //if there is no file uploaded, an alert will be shown to the user and an exception will be thrown.
            Alert fileNotChosen = new Alert(Alert.AlertType.ERROR,"Please upload a file first.");
            fileNotChosen.showAndWait();
            throw new Exception("Please upload a file!");
        }
        //set newImage as false
        newImage=false;




    }
    @FXML
    private void DownSize(ActionEvent e) throws Exception {
        //downsize the file to 100*100
        if (this.file !=null){
            imageResult.setPreserveRatio(true);
            imageModified = new Image(file.toURI().toString(), 100, 100, false, false);
            imageResult.setImage(imageModified);
            //renew width and height
            width = imageModified.getWidth();
            height = imageModified.getHeight();
            //write to log
            WriteToFile("Downsize");

        }else{
            //if there is no file uploaded, an alert will be shown to the user and an exception will be thrown.
            Alert fileNotChosen = new Alert(Alert.AlertType.ERROR,"Please upload a file first.");
            fileNotChosen.showAndWait();
            throw new Exception("Please upload a file!");
        }
        //set newImage as false
        newImage=false;



    }
    @FXML
    private void ConvertToJPG(ActionEvent e) throws Exception {
        if(imageModified!=null) {
            //if image has been modified, the modified image will be saved to jpg file, and newImage will be set to true
            Image imageToBeSaved = imageResult.snapshot(null,null);
            BufferedImage awtImage = new BufferedImage((int)imageToBeSaved.getWidth(), (int)imageToBeSaved.getHeight(), BufferedImage.TYPE_INT_RGB);
            String fileNameWithOutExt = file.getName().replaceFirst("[.][^.]+$","");
            ImageIO.write(SwingFXUtils.fromFXImage(imageToBeSaved, awtImage),"jpeg",new File("Output/"+fileNameWithOutExt+"result.jpeg"));
            newImage=true;
            WriteToFile("Convert to JPG");
        }else{
            //if there is no change to the file, an alert will be shown to the user and an exception will be thrown.
            Alert fileNotChosen = new Alert(Alert.AlertType.ERROR,"Nothing has been changed, so nothing will be saved.");
            fileNotChosen.showAndWait();
            throw new Exception("Nothing has been changed, so nothing will be saved.");
        }
    }

    @FXML
    private void ConvertToPNG(ActionEvent e) throws Exception {
        if(imageModified!=null) {
            //if image has been modified, the modified image will be saved to png file, and newImage will be set to true
            Image imageToBeSaved = imageResult.snapshot(null,null);
            String fileNameWithOutExt = file.getName().replaceFirst("[.][^.]+$","");
            ImageIO.write(SwingFXUtils.fromFXImage(imageToBeSaved, null),"png",new File("Output/"+fileNameWithOutExt+"result.png"));
            newImage=true;
            WriteToFile("Convert to PNG");
        }else{
            //if there is no change to the file, an alert will be shown to the user and an exception will be thrown.
            Alert fileNotChosen = new Alert(Alert.AlertType.ERROR,"Nothing has been changed, so nothing will be saved.");
            fileNotChosen.showAndWait();
            throw new Exception("Nothing has been changed, so nothing will be saved.");
        }
    }

    @FXML
    private void Restore(ActionEvent e) throws IOException {
        //original image will be shown if user clicks this button
        imageModified = new Image(file.toURI().toString());
        ColorAdjust original = new ColorAdjust();
        original.setSaturation(0.0);
        imageResult.setEffect(original);
        imageResult.setImage(imageModified);
        WriteToFile("Restore");
    }

    @FXML
    private void WriteToFile(String s) throws IOException {
        //log will be generated through this function
        FileWriter f = new FileWriter("log.txt",true);
        PrintWriter pw = new PrintWriter(f);
        pw.println(s);
        pw.close();
    }

}
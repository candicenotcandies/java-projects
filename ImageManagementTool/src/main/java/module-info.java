module project.imagemanagementtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens project.imagemanagementtool to javafx.fxml;
    exports project.imagemanagementtool;
}
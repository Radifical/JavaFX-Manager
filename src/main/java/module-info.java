module com.example.loginscreen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires itextpdf;


    opens com.example.loginscreen to javafx.fxml;
    exports com.example.loginscreen;
}
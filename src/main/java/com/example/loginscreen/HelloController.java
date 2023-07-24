package com.example.loginscreen;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


public class HelloController {

    //login
    @FXML
    private Button cancelButton;
    @FXML
    private Text loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button openManager;
    private studentManagerController sm;
    private Stage stage;
    private Scene scene;
    private Parent root;

    //Student Adder
    @FXML
    private Label dateText;




    public void switchToStudentManager(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("studentManager.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    public void loginButtonOnAction(ActionEvent e){


        if(usernameTextField.getText().isBlank() == false && passwordPasswordField.getText().isBlank() == false){
            //loginMessageLabel.setText(("You tried to login."));
            validateLogin();
        }
        else {
            loginMessageLabel.setText(("Enter login info."));
        }
    }
    @FXML
    private void cancelButtonOnAction(ActionEvent e){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM UserAccounts WHERE username = '" + usernameTextField.getText() + "' AND password = '" + passwordPasswordField.getText() +"'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1)==1){
                    openManager.setDisable(false);
                    openManager.setVisible(true);
                }else{
                    loginMessageLabel.setText("Invalid Login");
                }
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
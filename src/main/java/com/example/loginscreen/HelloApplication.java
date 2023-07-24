package com.example.loginscreen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            String name = "Radif";

            String q = "UPDATE studentdata SET studentclasses = studentclasses-1 WHERE studentname ="
                    +" '"+name +"' ";
            String q1 = "SELECT * FROM studentdata WHERE studentname="
                    +" '"+name +"' ";

            System.out.println(q);
            System.out.println(q1);


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
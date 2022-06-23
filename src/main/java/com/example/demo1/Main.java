package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("earthGui.fxml"));
        stage.setTitle("OBIS aplication");
        Scene scene = new Scene(content);
        stage.setScene(scene);

        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
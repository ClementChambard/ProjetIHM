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
        stage.setScene(new Scene(content));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
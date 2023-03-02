package org.example.userform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            System.out.println(HelloApplication.class.getClassLoader().getResource("LoginUser.fxml"));
            System.out.println();
            fxmlLoader.setLocation(HelloApplication.class.getClassLoader().getResource("LoginUser.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
//            Parent root = FXMLLoader.load(getClass().getResource(("LoginUser.fxml")));
//            stage.setTitle("Rock Paper Scissor RPG V0.1");
//            stage.setScene(new Scene(root, 600, 400));
//            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}

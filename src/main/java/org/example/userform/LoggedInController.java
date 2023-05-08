package org.example.userform;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private Button button_start, button_logout;
    @FXML
    private Label label_text_cloud, label_title;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_start.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                    Stage stage = (Stage) button_start.getScene().getWindow();
//                    stage.close();
//                        fireNewGame();
//                    stage.show();
//                        fireExitToMainMenu();
                        label_text_cloud.setText("Hi " + " Success");
                    }
                });
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    DBUtils.changeScene(event, "LoginUser.fxml", "Login", null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setUserData(String username) {
        label_text_cloud.setText("Hi " + username + "!");
        label_title.setText("Rock Paper Scissor RPG V0.1");
    }

//    private void switchToMainMenu(){
//        Stage stage = (Stage) closeButton.getScene().getWindow();
//        stage.close();
//        fireNewGame();
////        fireExitToMainMenu();
////        String[] a = {""};
////        MainGame.launch(a);
//    }
}

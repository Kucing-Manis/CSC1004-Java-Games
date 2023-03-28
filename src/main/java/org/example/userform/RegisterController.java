package org.example.userform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private Button button_register, button_back;
    @FXML
    private TextField tf_username, tf_email, tf_password, tf_confirm_password;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    if(tf_password.getText().equals(tf_confirm_password.getText())) {
                        DBUtils.register(event, tf_username.getText(), tf_email.getText(), tf_password.getText());
                    } else {
                        System.out.println("Password not the same");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password Is Not The Same");
                        alert.setContentText("Password is not the same as Confirmed Password\nPlease check whether the password is the same as the confirmed password");
                        alert.show();
                    }
                } else {
                    System.out.println("Please fill in the information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Missing");
                    alert.setContentText("Please fill in all the information");
                    alert.show();
                }

            }
        });
        Image img = new Image("assets/menu/Back.png");
        ImageView button_back_image = new ImageView(img);
        button_back_image.setFitHeight(45);
        button_back_image.setPreserveRatio(true);
        button_back.setGraphic(button_back_image);
        button_back.setOnAction(new EventHandler<ActionEvent>() {
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
}

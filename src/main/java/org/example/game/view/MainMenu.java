package org.example.game.view;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.userform.DBUtils;
import org.example.userform.HelloApplication;

import java.io.IOException;


public class MainMenu extends FXGLMenu {

    public MainMenu() {
        super(MenuType.MAIN_MENU);

//        Media media = new Media(getClass().getClassLoader().getResource("/assets/sounds/mainMenu.wav").toExternalForm());
//        MediaPlayer mainMenuSound = new MediaPlayer(media);
//        mainMenuSound.play();
//        mainMenuSound.setCycleCount(MediaPlayer.INDEFINITE);

        var bg = new Rectangle(getAppWidth(), getAppHeight(), Color.BLACK);

        var title = FXGL.getUIFactoryService().newText("Rock Paper Scissor RPG", Color.LIME, FontType.MONO, 35);
        var startButton = new Button("Start Game");
        var quitButton = new Button("Quit");
        var loginButton = new Button("Login");

        startButton.setMinSize(200, 100);
        quitButton.setMinSize(200, 100);
        loginButton.setMinSize(200, 100);

        title.setTranslateY(100);
        title.setTranslateX(getAppWidth() / 2 - 175);

        startButton.setTranslateY(350);
        startButton.setTranslateX(getAppWidth() / 2 - 100);
        startButton.setStyle("-fx-font-size:25");

        quitButton.setTranslateY(490);
        quitButton.setTranslateX(getAppWidth() / 2 - 100);
        quitButton.setStyle("-fx-font-size:25");

        quitButton.setTranslateY(630);
        quitButton.setTranslateX(getAppWidth() / 2 - 100);
        quitButton.setStyle("-fx-font-size:25");

        startButton.setOnAction(event -> {
            fireNewGame();
//            mainMenuSound.stop();
        });

        quitButton.setOnAction(event -> fireExit());

        loginButton.setOnAction((ActionEvent event)->{
            try {
                DBUtils.changeScene(event, "LoginUser.fxml", "Register", null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        getContentRoot().getChildren().addAll(
                bg, title, startButton, quitButton, loginButton
        );
    }
}


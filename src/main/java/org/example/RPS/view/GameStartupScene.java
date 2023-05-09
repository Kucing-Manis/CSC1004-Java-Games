package org.example.RPS.view;

import com.almasb.fxgl.app.scene.StartupScene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class GameStartupScene extends StartupScene {
    public GameStartupScene(int appWidth, int appHeight) {
        super(appWidth, appHeight);
        StackPane pane = new StackPane(new ImageView(getClass().getClassLoader().getResource("assets/textures/menu/Character.png").toExternalForm()));
        pane.setPrefSize(appWidth, appHeight);
        pane.setStyle("-fx-background-color: black");
        getContentRoot().getChildren().addAll(pane);
    }
}

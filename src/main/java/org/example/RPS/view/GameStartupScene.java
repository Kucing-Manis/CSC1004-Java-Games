package org.example.RPS.view;

import com.almasb.fxgl.app.scene.StartupScene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class GameStartupScene extends StartupScene {
    public GameStartupScene(int appWidth, int appHeight) {
        super(appWidth, appHeight);
        StackPane pane = new StackPane(new ImageView(getClass().getClassLoader().getResource("assets/textures/menu/Character.png").toExternalForm()));
        // Add Rock Paper Scissor RPG logo image
        // Maybe Add Rock Paper Scissor image
        pane.setPrefSize(appWidth, appHeight);
        pane.setStyle("-fx-background-color: black");
//        ImageView image1 = new ImageView(new Image(GameStartupScene.class.getClassLoader().getResourceAsStream("assets/images/menu/BackgroundCharacter.jpeg")));
//        ImageView image2 = new ImageView(new Image(GameStartupScene.class.getClassLoader().getResourceAsStream("assets/images/menu/Character.png")));
//        image1.relocate(10, 10);
//        image2.relocate(80, 60);
        getContentRoot().getChildren().addAll(pane);
    }

//    Pane pane = new Pane();
//  pane.setStyle("-fx-background-color: linear-gradient(to bottom right, derive(goldenrod, 20%), derive(goldenrod, -40%));");
//    ImageView iv1 = new ImageView(new Image("http://icons.iconarchive.com/icons/kidaubis-design/cool-heroes/128/Ironman-icon.png"));  // Creative commons with attribution license for icons: No commercial usage without authorization. All rights reserved. Design (c) 2008 - Kidaubis Design http://kidaubis.deviantart.com/  http://www.kidcomic.net/ All Rights of depicted characters belong to their respective owners.
//    ImageView iv2 = new ImageView(new Image("http://icons.iconarchive.com/icons/kidaubis-design/cool-heroes/128/Starwars-Stormtrooper-icon.png"));
//  iv1.relocate(10, 10);
//  iv2.relocate(80, 60);
//  pane.getChildren().addAll(iv1, iv2);
//  stage.setScene(new Scene(pane));
//  stage.show();
}

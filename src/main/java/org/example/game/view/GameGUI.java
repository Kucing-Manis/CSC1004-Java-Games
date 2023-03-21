package org.example.game.view;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXMLLoader;
import org.example.game.view.MainMenu;
import org.example.userform.HelloApplication;
import org.jetbrains.annotations.NotNull;

public class GameGUI {
    public static final int width = 1280;
    public static final int height = 720;

    public void initSettings(GameSettings settings){
        settings.setWidth(width);
        settings.setHeight(height);
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory(){
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new MainMenu();
            }
        });

        settings.setTicksPerSecond(60);
// check info : settings.setProfilingEnabled(true);
//        settings.setFontUI("arcade_classic.ttf");
//        settings.setFontText("arcade_classic.ttf");
//        settings.setFontGame("arcade_classic.ttf");
//        settings.setFontMono("arcade_classic.ttf");
    }
}

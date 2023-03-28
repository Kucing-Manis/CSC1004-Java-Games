package org.example.RPS.view;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.StartupScene;

public class GameGUI {
    public static final int width = 28 * 24 + 6 * 24;
    public static final int height = 28 * 24;

    public void initSettings(GameSettings settings){
        settings.setWidth(width);
        settings.setHeight(height);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory(){
            @Override
            public StartupScene newStartup(int width, int height) {
                //自定义启动场景
                return new GameStartupScene(width, height);
            }
            @Override
            public FXGLMenu newMainMenu() {
                return new GameMainMenu();
            }
            @Override
            public LoadingScene newLoadingScene() {
                //游戏前的加载场景
                return new GameLoadingScene();
            }
        });

//        settings.setTicksPerSecond(60);
// check info : settings.setProfilingEnabled(true);
//        settings.setFontUI("arcade_classic.ttf");
//        settings.setFontText("arcade_classic.ttf");
//        settings.setFontGame("arcade_classic.ttf");
//        settings.setFontMono("arcade_classic.ttf");
    }
}

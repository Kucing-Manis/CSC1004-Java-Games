package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.UI;
import com.almasb.fxgl.ui.UIController;
import javafx.fxml.FXML;
import org.example.game.animation.AnimationComponent;
import org.example.game.animation.SpriteData;
import org.example.game.view.GameGUI;

import static com.almasb.fxgl.app.GameApplication.launch;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class MainGame extends GameApplication {
        private GameApplication app;
        GameGUI gui = new GameGUI();
//    DinosaurController controller = new DinosaurController();
    public void runGame() {
//        GameApplication app2 = MainGame.newInstance();
//        Stage anotherStage = new Stage();
//        app2.start(anotherStage);
//        start(anotherStage);
//        String[] a = {""};
//        launch(a);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        gui.initSettings(settings);
        settings.setTitle("Rock Paper Scissor RPG");
        settings.setVersion("0.1");
//        settings.setAppIcon();

    }
    private Entity character1, character2, character3, character4, character5;

    @Override
    protected void initInput() {

    }

    @Override
    protected void initGame() {
//        SpriteData spriteData = new SpriteData("sprite/HeroKnight.png", 10, 100, 55, 1, 0,7);

        character1 = FXGL.entityBuilder()
                .at(150, 370)
                .with(new AnimationComponent(new SpriteData("sprite/HeroKnight.png", 10, 100, 55, 0, 7, 1)))
                .buildAndAttach();
        character2 = FXGL.entityBuilder()
                .at(135, 570)
                .with(new AnimationComponent(new SpriteData("sprite/GroundMonk.png", 14, 288, 128, 0, 6, 1)))
                .buildAndAttach();
        character3 = FXGL.entityBuilder()
                .at(175, 170)
                .with(new AnimationComponent(new SpriteData("sprite/NightBorne.png", 5, 75, 80, 0, 9, 1)))
                .buildAndAttach();
//        character4 = FXGL.entityBuilder()
//                .at(200, 200)
//                .with(new AnimationComponent(new SpriteData("sprite/HeroKnight.png", 10, 100, 55, 0, 7,1)))
//                .buildAndAttach();
//        character5 = FXGL.entityBuilder()
//                .at(200, 200)
//                .with(new AnimationComponent(new SpriteData("sprite/HeroKnight.png", 10, 100, 55, 0, 7,1)))
//                .buildAndAttach();


    }

//    @Override
//    protected void initUI(){
//        GameUIController controller = new GameUIController();
//        UI fxmlUI = getAssetLoader().loadUI("../LoginUser.fxml", controller);
//        getGameScene().addUI(fxmlUI);
//    }

    public static class GameUIController implements UIController {
        @Override
        public void init() { }
    }
    public static void main(String[] args) {
        launch(args);
    }

}



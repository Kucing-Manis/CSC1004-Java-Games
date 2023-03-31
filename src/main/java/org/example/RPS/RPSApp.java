package org.example.RPS;

import com.almasb.fxgl.app.CursorInfo;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.ui.UIController;
import javafx.scene.input.KeyCode;
import org.example.RPS.animation.AnimationComponent;
import org.example.RPS.animation.SpriteData;
import org.example.RPS.view.GameGUI;

import static com.almasb.fxgl.app.GameApplication.launch;

public class RPSApp extends GameApplication {
        private GameApplication app;
        GameGUI gui = new GameGUI();
//    DinosaurController controller = new DinosaurController();

    @Override
    protected void initSettings(GameSettings settings) {
        gui.initSettings(settings);
        settings.setTitle("Rock Paper Scissor RPG");
        settings.setVersion("0.1");
//        settings.setAppIcon();
        settings.getCSSList().add("RPSApp.css");
        settings.setDefaultCursor(new CursorInfo("ui/cursor.png", 0, 0));

    }
    private Entity character1, character2, character3, character4, character5;

    @Override
    protected void initInput() {
//         Movement LEFT
        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                character1.getComponent(AnimationComponent.class).moveLeft();
                character2.getComponent(AnimationComponent.class).moveLeft();
                character3.getComponent(AnimationComponent.class).moveLeft();
            }
        }, KeyCode.A);
        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                character1.getComponent(AnimationComponent.class).moveRight();
                character2.getComponent(AnimationComponent.class).moveRight();
                character3.getComponent(AnimationComponent.class).moveRight();
            }
        }, KeyCode.D);
    }

    @Override
    protected void initGame() {
//        SpriteData spriteHero = new SpriteData("sprite/Hero.png", 6, 6, 64, 64, 0, 9, 1);

//        character1 = FXGL.entityBuilder()
//                .at(185, 360)
//                .with(new AnimationComponent(new SpriteData("Hero.png", 6, 64, 64, 0, 9, 1)))
//                .buildAndAttach();
//        character2 = FXGL.entityBuilder()
//                .at(150, 410)
//                .with(new AnimationComponent(new SpriteData("Wizard.png", 6, 64, 64, 0, 9, 1)))
//                .buildAndAttach();
//        character3 = FXGL.entityBuilder()
//                .at(150, 310)
//                .with(new AnimationComponent(new SpriteData("Healer.png", 6, 64, 64, 0, 9, 1)))
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



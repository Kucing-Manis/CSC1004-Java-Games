package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import org.example.game.animation.AnimationComponent;
import org.example.game.animation.SpriteData;

public class Main extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Rock Paper Scissor RPG V0.1");
        settings.setVersion("0.1");
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
                .with(new AnimationComponent(new SpriteData("sprite/HeroKnight.png", 10, 100, 55, 0, 7,1)))
                .buildAndAttach();
        character2 = FXGL.entityBuilder()
                .at(135, 570)
                .with(new AnimationComponent(new SpriteData("sprite/GroundMonk.png", 14, 288, 128, 0, 6,1)))
                .buildAndAttach();
        character3 = FXGL.entityBuilder()
                .at(175, 170)
                .with(new AnimationComponent(new SpriteData("sprite/NightBorne.png", 5, 75, 80, 0, 9,1)))
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

    public static void main(String[] args) {
        launch(args);
    }
}
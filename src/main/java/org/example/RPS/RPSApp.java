package org.example.RPS;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.CursorInfo;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.RPS.animation.AnimationComponent;
import org.example.RPS.animation.SpriteData;
import org.example.RPS.component.CharacterComponent;
import org.example.RPS.view.GameSettings;

import java.util.ArrayList;

import static com.almasb.fxgl.app.GameApplication.launch;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.addUINode;

public class RPSApp extends GameApplication {
    private GameApplication app;
    GameSettings setting = new GameSettings();
//    DinosaurController controller = new DinosaurController();

    private Entity p1, p2, p3;
//    private CharacterComponent heroComp, wizardComp, healerComp, guardComp, fighterComp;
    private ArrayList<CharacterComponent> characterComps = new ArrayList<>();
    private CharacterComponent actingCharacter;
    private Entity rock, paper, scissor;
    private Entity box, box2;

    @Override
    protected void initSettings(com.almasb.fxgl.app.GameSettings settings) {
        setting.initSettings(settings);
        // GameSettings hapus g berguna
        settings.setTitle("Rock Paper Scissor RPG");
        settings.setVersion("0.1");
//        settings.setAppIcon();
        settings.getCSSList().add("RPSApp.css");
        settings.setDefaultCursor(new CursorInfo("ui/cursor.png", 0, 0));

    }

    @Override
    protected void initInput() {
//         Movement LEFT
        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                p1.getComponent(AnimationComponent.class).moveLeft();
                p2.getComponent(AnimationComponent.class).moveLeft();
                p3.getComponent(AnimationComponent.class).moveLeft();
            }
        }, KeyCode.A);
        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                p1.getComponent(AnimationComponent.class).moveRight();
                p2.getComponent(AnimationComponent.class).moveRight();
                p3.getComponent(AnimationComponent.class).moveRight();
            }
        }, KeyCode.D);
    }

    @Override
    protected void initGame() {
        // Set Background
        getGameWorld().addEntityFactory(new GameEntityFactory());
        spawn("background");

        // Sprite Data
        SpriteData spriteHero = new SpriteData("characters/Hero.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteWizard = new SpriteData("characters/Wizard.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteHealer = new SpriteData("characters/Healer.png", 6,64, 64, 0, 9, 1);
        SpriteData spriteGuard = new SpriteData("characters/Guard.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteFighter = new SpriteData("characters/Fighter.png", 6, 64, 64, 0, 9, 1);

        //Character
        characterComps.add(new CharacterComponent("Hero", "Justice", 1,10,1,2,1, spriteHero));
        characterComps.add(new CharacterComponent("Wizard", "Fireball", 1,5,1,3,1, spriteWizard));
        characterComps.add(new CharacterComponent("Healer", "Heal", 1,6,1,1,1, spriteHealer));
        characterComps.add(new CharacterComponent("Guard", "Guard Team", 1,15,1,1,1, spriteGuard));
        characterComps.add(new CharacterComponent("Fighter", "Pierce", 1,7,1,3,1, spriteFighter));

//        heroComp = new CharacterComponent("Hero", "Justice", 1,1,1,1,1 );
//        heroComp.setDamage((int)heroComp.getLevel()/5 + heroComp.getDamage());
//        heroComp.setHp((int)heroComp.getLevel()/3 + heroComp.getHp());
//        wizardComp = new CharacterComponent("Hero", "Justice", 1,1,1,1,1 );
//        heroComp.setDamage((int)heroComp.getLevel()/5 + heroComp.getDamage());
//        heroComp.setHp((int)heroComp.getLevel()/3 + heroComp.getHp());
//        healerComp = new CharacterComponent("Hero", "Justice", 1,1,1,1,1 );
//        heroComp.setDamage((int)heroComp.getLevel()/5 + heroComp.getDamage());
//        heroComp.setHp((int)heroComp.getLevel()/3 + heroComp.getHp());

        p1 = FXGL.entityBuilder()
                .at(185, 360)
                .with(new AnimationComponent(new SpriteData("characters/Hero.png", 6, 64, 64, 0, 9, 1)))
                .buildAndAttach();
        p2 = FXGL.entityBuilder()
                .at(150, 410)
                .with(new AnimationComponent(new SpriteData("characters/Wizard.png", 6, 64, 64, 0, 9, 1)))
                .buildAndAttach();
        p3 = FXGL.entityBuilder()
                .at(150, 310)
                .with(new AnimationComponent(new SpriteData("characters/Healer.png", 6, 64, 64, 0, 9, 1)))
                .buildAndAttach();
        box = FXGL.entityBuilder().at(200, 100).view(new Rectangle(50, 50, Color.BLACK)).buildAndAttach();
        box2 = FXGL.entityBuilder().at(600, 100).view(new Rectangle(50, 50, Color.BLACK)).buildAndAttach();
        box.setVisible(false);
        box2.setVisible(false);

        rock = FXGL.entityBuilder().at(200, 600).view("ui/Rock.png").buildAndAttach();
        paper = FXGL.entityBuilder().at(400, 600).view("ui/Paper.jpeg").buildAndAttach();
        scissor = FXGL.entityBuilder().at(600, 600).view("ui/Scissor.png").buildAndAttach();
        rock.setScaleX(0.27);
        rock.setScaleY(0.27);
        paper.setScaleX(0.27);
        paper.setScaleY(0.27);
        scissor.setScaleX(0.27);
        scissor.setScaleY(0.27);

        rock.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            calculateNormalEnemyRPS("rock");
        });
        paper.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            calculateNormalEnemyRPS("paper");
        });
        scissor.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            calculateNormalEnemyRPS("scissor");
        });
        box.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            initRPS();
        });
        box2.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            initRPS();
        });
        p1.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {

        });
        p2.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {

        });
        p3.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {

        });
        initRPS();
    }

    protected void initRPS() {
        rock.setVisible(true);
        paper.setVisible(true);
        scissor.setVisible(true);
        box.setVisible(false);
        box2.setVisible(false);
    }

    protected void initRPS(CharacterComponent character) {
        rock.setVisible(true);
        paper.setVisible(true);
        scissor.setVisible(true);
        box.setVisible(false);
        box2.setVisible(false);
        actingCharacter = character;
    }

    protected void calculateNormalEnemyRPS(String playerPick) {
        // Coding for Enemy Random Pick RPS
        double random = Math.random();
        String enemyPick = "rock";
        if (random <= 0.333333333333) {
            enemyPick = "paper";
        } else if (random <= 0.6666666666) {
            enemyPick = "scissor";
        }

        // Check Player vs Enemy in RPS (Win or Draw or Lose)
        String resultRPS = "";
        if (playerPick.equals("rock")) {
            if (enemyPick.equals("rock")) {
                resultRPS = "Draw";
                System.out.println(playerPick + " + " + enemyPick + " = Draw");
            } else if (enemyPick.equals("paper")) {
                resultRPS = "Lose";
                System.out.println(playerPick + " + " + enemyPick + " = Lose");
            } else {
                resultRPS = "Win";
                System.out.println(playerPick + " + " + enemyPick + " = Win");
            }
        } else if (playerPick.equals("paper")) {
            if (enemyPick.equals("rock")) {
                resultRPS = "Win";
                System.out.println(playerPick + " + " + enemyPick + " = Win");
            } else if (enemyPick.equals("paper")) {
                resultRPS = "Draw";
                System.out.println(playerPick + " + " + enemyPick + " = Draw");
            } else {
                resultRPS = "Lose";
                System.out.println(playerPick + " + " + enemyPick + " = Lose");
            }
        } else {
            if (enemyPick.equals("rock")) {
                resultRPS = "Lose";
                System.out.println(playerPick + " + " + enemyPick + " = Lose");
            } else if (enemyPick.equals("paper")) {
                resultRPS = "Win";
                System.out.println(playerPick + " + " + enemyPick + " = Win");
            } else {
                resultRPS = "Draw";
                System.out.println(playerPick + " + " + enemyPick + " = Draw");
            }
        }
        TransitionRPS(playerPick, enemyPick, resultRPS);
        rock.setVisible(false);
        paper.setVisible(false);
        scissor.setVisible(false);
    }

    protected void TransitionRPS(String playerType, String enemyType, String inputResult) {
        Texture rockTT = FXGL.texture("ui/Rock.png");
        Texture paperTT = FXGL.texture("ui/Paper.jpeg");
        Texture scissorTT = FXGL.texture("ui/Scissor.png");
        rockTT.setScaleX(0.35);
        rockTT.setScaleY(0.35);
        paperTT.setScaleX(0.35);
        paperTT.setScaleY(0.35);
        scissorTT.setScaleX(0.35);
        scissorTT.setScaleY(0.35);
        rockTT.setOpacity(0.8);
        paperTT.setOpacity(0.8);
        scissorTT.setOpacity(0.8);
        rockTT.setVisible(false);
        paperTT.setVisible(false);
        scissorTT.setVisible(false);
        TranslateTransition ttRock = new TranslateTransition(Duration.seconds(2), rockTT);
        TranslateTransition ttPaper = new TranslateTransition(Duration.seconds(2), paperTT);
        TranslateTransition ttScissor = new TranslateTransition(Duration.seconds(2), scissorTT);
        if (playerType.equals("rock")) {
            rockTT.setVisible(true);
            ttRock.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
            ttRock.setFromX(-400);
            ttRock.setFromY(getAppHeight() / 2.0);
            ttRock.setToX(getAppWidth() / 2.0 - 400);
            ttRock.setToY(getAppHeight() / 2.0);
            ttRock.setOnFinished(e -> {
                enemyTransition(enemyType, inputResult);
            });
            ttRock.play();
        } else if (playerType.equals("paper")) {
            paperTT.setVisible(true);
            ttPaper.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
            ttPaper.setFromX(-400);
            ttPaper.setFromY(getAppHeight() / 2.0);
            ttPaper.setToX(getAppWidth() / 2.0 - 415);
            ttPaper.setToY(getAppHeight() / 2.0 - 13);
            ttPaper.setOnFinished(e -> {
                enemyTransition(enemyType, inputResult);
            });
            ttPaper.play();
        } else if (playerType.equals("scissor")) {
            scissorTT.setVisible(true);
            ttScissor.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
            ttScissor.setFromX(-400);
            ttScissor.setFromY(getAppHeight() / 2.0);
            ttScissor.setToX(getAppWidth() / 2.0 - 400);
            ttScissor.setToY(getAppHeight() / 2.0);
            ttScissor.setOnFinished(e -> {
                enemyTransition(enemyType, inputResult);
            });
            ttScissor.play();
        } else {
            rockTT.setVisible(false);
            paperTT.setVisible(false);
            scissorTT.setVisible(false);
        }

        Pane pane = new Pane(rockTT, paperTT, scissorTT);
        addUINode(pane);

        // Timer to erase the pane
        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(5.1)
                ));
        tl.setOnFinished(e -> {
            removeUINode(pane);
//            System.out.println("Finish Player Transition");
        });
        tl.play();
    }

    protected void enemyTransition(String enemy, String input) {
        // Set Text
        Text result = new Text();
        result.setText(input);
        result.setFill(Color.BLACK);
        result.setFont(new Font(50));
        result.setLayoutX(getAppWidth() / 2.0 - 80);
        result.setLayoutY(getAppHeight() / 2.0 - 100);
        result.setVisible(false);

        // Get Image
        Texture rockTT = FXGL.texture("ui/Rock.png");
        Texture paperTT = FXGL.texture("ui/Paper.jpeg");
        Texture scissorTT = FXGL.texture("ui/Scissor.png");
        rockTT.setScaleX(-0.35);
        rockTT.setScaleY(0.35);
        paperTT.setScaleX(-0.35);
        paperTT.setScaleY(0.35);
        scissorTT.setScaleX(-0.35);
        scissorTT.setScaleY(0.35);
        rockTT.setOpacity(0.8);
        paperTT.setOpacity(0.8);
        scissorTT.setOpacity(0.8);
        rockTT.setVisible(false);
        paperTT.setVisible(false);
        scissorTT.setVisible(false);
        TranslateTransition ttRock = new TranslateTransition(Duration.seconds(2), rockTT);
        TranslateTransition ttPaper = new TranslateTransition(Duration.seconds(2), paperTT);
        TranslateTransition ttScissor = new TranslateTransition(Duration.seconds(2), scissorTT);
        // Check Enemy Pick and Translation
        if (enemy.equals("rock")) {
            rockTT.setVisible(true);
            ttRock.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
            ttRock.setFromX(1200);
            ttRock.setFromY(getAppHeight() / 2.0);
            ttRock.setToX(getAppWidth() / 2.0 - 50);
            ttRock.setToY(getAppHeight() / 2.0);
            ttRock.setOnFinished(e -> {
                result.setVisible(true);
            });
            ttRock.play();
        } else if (enemy.equals("paper")) {
            paperTT.setVisible(true);
            ttPaper.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
            ttPaper.setFromX(1200);
            ttPaper.setFromY(getAppHeight() / 2.0 - 13);
            ttPaper.setToX(getAppWidth() / 2.0 - 65);
            ttPaper.setToY(getAppHeight() / 2.0 - 13);
            ttPaper.setOnFinished(e -> {
                result.setVisible(true);
            });
            ttPaper.play();
        } else if (enemy.equals("scissor")) {
            scissorTT.setVisible(true);
            ttScissor.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
            ttScissor.setFromX(1200);
            ttScissor.setFromY(getAppHeight() / 2.0);
            ttScissor.setToX(getAppWidth() / 2.0 - 50);
            ttScissor.setToY(getAppHeight() / 2.0);
            ttScissor.setOnFinished(e -> {
                result.setVisible(true);
            });
            ttScissor.play();
        } else {
            rockTT.setVisible(false);
            paperTT.setVisible(false);
            scissorTT.setVisible(false);
            result.setVisible(false);
        }

        Pane pane = new Pane(result, rockTT, paperTT, scissorTT);
        addUINode(pane);

        // Timer to erase the pane and activate battleCharacter()
        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(3.1)
                ));
        tl.setOnFinished(e -> {
//            System.out.println("Finish Enemy Transition");
            removeUINode(pane);
            battleCharacter(input);
        });
        tl.play();
    }

    protected void battleCharacter(String result) {
        System.out.println("Battle Time");
        box.setVisible(true);
        box2.setVisible(true);
    }


    public static void main(String[] args) {
        launch(args);
    }

}



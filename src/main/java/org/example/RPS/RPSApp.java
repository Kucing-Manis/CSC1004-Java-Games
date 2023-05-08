package org.example.RPS;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.CursorInfo;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;

public class RPSApp extends GameApplication {
    private GameApplication app;
    GameSettings setting = new GameSettings();
    private HealthIntComponent hp1, hp2, hp3, shield1, shield2, shield3;
    private Entity p1, p2, p3, enemy;
    private Entity rock, paper, scissor;
    private Entity boxPanel1, highlight;
//    private CharacterComponent heroComp, wizardComp, healerComp, guardComp, fighterComp;
    private ArrayList<CharacterComponent> characterComps = new ArrayList<>();
    private ArrayList<CharacterComponent> enemyComps = new ArrayList<>();
    private CharacterComponent actingCharacter;
    private int selectedBox = 0;
    private int stage = 1;
    private int round = 1;
    private Text labelStage, labelInfo;
    private Button nextAction;

//    private String action;

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
//        FXGL.getInput().addAction(new UserAction("Left") {
//            @Override
//            protected void onAction() {
//                p1.getComponent(AnimationComponent.class).moveLeft();
//                p2.getComponent(AnimationComponent.class).moveLeft();
//                p3.getComponent(AnimationComponent.class).moveLeft();
//            }
//        }, KeyCode.A);
//        FXGL.getInput().addAction(new UserAction("Right") {
//            @Override
//            protected void onAction() {
//                p1.getComponent(AnimationComponent.class).moveRight();
//                p2.getComponent(AnimationComponent.class).moveRight();
//                p3.getComponent(AnimationComponent.class).moveRight();
//            }
//        }, KeyCode.D);
    }

    @Override
    protected void initGame() {
        // Set Background
        getGameWorld().addEntityFactory(new GameEntityFactory());
        spawn("background");

//        getGameWorld().create("box", new SpawnData(0, 0).put("box", )))
        // Sprite Data
        SpriteData spriteHero = new SpriteData("characters/Hero.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteWizard = new SpriteData("characters/Wizard.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteHealer = new SpriteData("characters/Healer.png", 6,64, 64, 0, 9, 1);
        SpriteData spriteGuard = new SpriteData("characters/Guard.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteFighter = new SpriteData("characters/Fighter.png", 6, 64, 64, 0, 9, 1);

        //Character
        characterComps.add(new CharacterComponent("Hero", "Justice", "", 1,10,1,2, spriteHero));
        characterComps.add(new CharacterComponent("Wizard", "Fireball", "", 1,5,1,3, spriteWizard));
        characterComps.add(new CharacterComponent("Healer", "Heal", "",1,6,1,1, spriteHealer));
        characterComps.add(new CharacterComponent("Guard", "Guard Team", "", 1,15,1,1, spriteGuard));
        characterComps.add(new CharacterComponent("Fighter", "Pierce", "", 1,7,1,3, spriteFighter));

        enemyComps.add(new CharacterComponent("Fighter", "Pierce", "", 1,7,1,3, spriteFighter));
//        heroComp = new CharacterComponent("Hero", "Justice", 1,1,1,1,1 );
//        heroComp.setDamage((int)heroComp.getLevel()/5 + heroComp.getDamage());
//        heroComp.setHp((int)heroComp.getLevel()/3 + heroComp.getHp());
//        wizardComp = new CharacterComponent("Hero", "Justice", 1,1,1,1,1 );
//        heroComp.setDamage((int)heroComp.getLevel()/5 + heroComp.getDamage());
//        heroComp.setHp((int)heroComp.getLevel()/3 + heroComp.getHp());
//        healerComp = new CharacterComponent("Hero", "Justice", 1,1,1,1,1 );
//        heroComp.setDamage((int)heroComp.getLevel()/5 + heroComp.getDamage());
//        heroComp.setHp((int)heroComp.getLevel()/3 + heroComp.getHp());

//        p1 = FXGL.entityBuilder()
//                .at(185, 500)
//                .with(new AnimationComponent(new SpriteData("characters/Hero.png", 6, 64, 64, 0, 9, 1)))
//                .buildAndAttach();
        p1 = FXGL.entityBuilder()
                .at(275, 460)
                .with(new AnimationComponent(characterComps.get(0).getSpriteData(), true))
                .buildAndAttach();
        p2 = FXGL.entityBuilder()
                .at(230, 400)
                .with(new AnimationComponent(characterComps.get(1).getSpriteData(), true))
                .buildAndAttach();
        p3 = FXGL.entityBuilder()
                .at(230, 520)
                .with(new AnimationComponent(characterComps.get(2).getSpriteData(), true))
                .buildAndAttach();

        enemy = FXGL.entityBuilder()
                .at(830, 460)
                .with(new AnimationComponent(enemyComps.get(0).getSpriteData(), false))
                .buildAndAttach();

        boxPanel1 = FXGL.entityBuilder().at(400, 100).view(new Rectangle(100, 100, Color.BLACK)).buildAndAttach();
        boxPanel1.setVisible(false);

        highlight = FXGL.entityBuilder().view(new Rectangle(194, 149, Color.WHITE)).buildAndAttach();
        highlight.setVisible(true);
        highlight.setOpacity(0.8);
        highlight.setPosition(3, 100 + 180*selectedBox - 12);

        labelInfo = new Text();;
        labelInfo.setFill(Color.BLACK);
        labelInfo.setFont(new Font(50));
        labelInfo.setLayoutX(getAppWidth() / 2.0 - 50);
        labelInfo.setLayoutY(getAppHeight() - 200);
        labelInfo.setVisible(false);

        boxPanel(characterComps.get(0), 0, true);
        boxPanel(characterComps.get(1), 1, true);
        boxPanel(characterComps.get(2), 2, true);
        boxPanel(enemyComps.get(0), 0, false);

        rock = FXGL.entityBuilder().at(200, 600).view("ui/Rock.png").buildAndAttach();
        paper = FXGL.entityBuilder().at(400, 600).view("ui/Paper.jpeg").buildAndAttach();
        scissor = FXGL.entityBuilder().at(600, 600).view("ui/Scissor.png").buildAndAttach();
        rock.setScaleX(0.27);
        rock.setScaleY(0.27);
        paper.setScaleX(0.27);
        paper.setScaleY(0.27);
        scissor.setScaleX(0.27);
        scissor.setScaleY(0.27);

        //RPS Click Function
        rock.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            calculateEnemyRPS("rock");
        });
        paper.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            calculateEnemyRPS("paper");
        });
        scissor.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            calculateEnemyRPS("scissor");
        });

        //Box Panel Click Function
        boxPanel1.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            initRPS();
        });

        //Players Click Function
        p1.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            actingCharacter = characterComps.get(0);
            selectedBox = 0;
            highlight.setPosition(3, 100 + 180*selectedBox - 12);
            System.out.println(actingCharacter.getName() + "   " + selectedBox);
        });
        p2.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            actingCharacter = characterComps.get(1);
            selectedBox = 1;
            highlight.setPosition(3, 100 + 180*selectedBox - 12);
            System.out.println(actingCharacter.getName() + "   " + selectedBox);
        });
        p3.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            actingCharacter = characterComps.get(2);
            selectedBox = 2;
            highlight.setPosition(3, 100 + 180*selectedBox - 12);
            System.out.println(actingCharacter.getName() + "   " + selectedBox);
        });
        initRPS();
    }

    protected void boxPanel(CharacterComponent CC, int num, boolean isPlayer){
        Rectangle bgRect = new Rectangle(190, 145);
        int x = 5;
        int y = 100 + (180*num);
        if(!isPlayer){
            x = getAppWidth() - 200;
        }
        bgRect.setLayoutX(x);
        bgRect.setLayoutY(y - 10);
        bgRect.setOpacity(0.8);
        Text levelName = getUIFactoryService().newText(   CC.getName() + " | Level " + CC.getLevel(), Color.WHITE, 18);
        Text skill = getUIFactoryService().newText( "Skill: " + CC.getSkillName() + "   ", Color.WHITE, 18);

        ProgressBar barHP = new ProgressBar(false);
        barHP.currentValueProperty().bind(CC.getHp().valueProperty());
        barHP.setMaxValue(CC.getMaxHp());
        barHP.setFill(Color.LIGHTGREEN);
        barHP.setWidth(200 / 3);
        barHP.setHeight(12.5);

        ProgressBar barShield = new ProgressBar(false);
        barShield.currentValueProperty().bind(CC.getShield().valueProperty());
        barShield.setMaxValue(CC.getMaxShield());
        barShield.setFill(Color.LIGHTGRAY);
        barShield.setWidth(200 / 3);
        barShield.setHeight(12.5);

        Text textHP = getUIFactoryService().newText( CC.getCurrentHp() + "", Color.WHITE, 18);
        Text textHP2 = getUIFactoryService().newText( " / " + CC.getMaxHp(), Color.WHITE, 18);
        textHP.textProperty().bind(CC.getHp().valueProperty().asString("%d"));

        Text textShield = getUIFactoryService().newText(CC.getCurrentShield() + "", Color.WHITE, 18);
        Text textShield2 = getUIFactoryService().newText(" / " + CC.getMaxShield(), Color.WHITE, 18);
        textShield.textProperty().bind(CC.getShield().valueProperty().asString("%d"));

        var boxHP = new HBox(-1, barHP, textHP, textHP2);
        var boxShield = new HBox(-1, barShield, textShield, textShield2);

        Button clickMe = new Button();
        clickMe.setId(num + "");
        clickMe.setText("Click Me");
        clickMe.setOnAction(event -> {
            actingCharacter = CC;
            selectedBox = num;
            highlight.setPosition(3, 100 + 180*selectedBox - 12);
            System.out.println(actingCharacter.getName() + "   " + selectedBox);
        });

        var boxSkill = new HBox(-1, skill, clickMe);

        VBox box = new VBox(6);//登录VBOX
        box.setAlignment(Pos.CENTER_LEFT);
        box.setLayoutX(x+8);
        box.setLayoutY(y);
        box.setVisible(true);

        box.getChildren().addAll(
                levelName,
                boxHP,
                boxShield,
                boxSkill);

        Pane pane = new Pane(bgRect, box);
        addUINode(pane);
    }
    protected void initRPS() {
        rock.setVisible(true);
        paper.setVisible(true);
        scissor.setVisible(true);
        boxPanel1.setVisible(false);
    }

    // Coding for Enemy Random Pick RPS
    protected String enemyNormalPick(){
        double random = Math.random();
        String enemyPick = "rock";
        if (random <= 0.333333333333) {
            enemyPick = "paper";
        } else if (random <= 0.6666666666) {
            enemyPick = "scissor";
        }
        return enemyPick;
    }
    protected void calculateEnemyRPS(String playerPick) {
        String enemyPick = enemyNormalPick();

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
        rockTT.setScaleX(0.3);
        rockTT.setScaleY(0.3);
        paperTT.setScaleX(0.3);
        paperTT.setScaleY(0.3);
        scissorTT.setScaleX(0.3);
        scissorTT.setScaleY(0.3);
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
            ttRock.setToX(getAppWidth() / 2.0 - 350);
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
            ttPaper.setToX(getAppWidth() / 2.0 - 365);
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
            ttScissor.setToX(getAppWidth() / 2.0 - 350);
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

        // Get Images
        Texture rockTT = FXGL.texture("ui/Rock.png");
        Texture paperTT = FXGL.texture("ui/Paper.jpeg");
        Texture scissorTT = FXGL.texture("ui/Scissor.png");
        rockTT.setScaleX(-0.3);
        rockTT.setScaleY(0.3);
        paperTT.setScaleX(-0.3);
        paperTT.setScaleY(0.3);
        scissorTT.setScaleX(-0.3);
        scissorTT.setScaleY(0.3);
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
            ttRock.setToX(getAppWidth() / 2.0 -100);
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
            ttPaper.setToX(getAppWidth() / 2.0 - 115);
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
            ttScissor.setToX(getAppWidth() / 2.0 - 100);
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
            battleTime(input);
        });
        tl.play();
    }

    protected void battleTime(String result) {
        boxPanel1.setVisible(true);
        labelInfo.setVisible(true);
        highlight.setVisible(true);

        System.out.println("Battle Time " + result);
        if(result.equals("Win")){
            labelInfo.setText("Pick your skills from the panel");
            initRPS();
        } else if(result.equals("Draw")){
            labelInfo.setText("Pick your skills from the panel");

            enemyAttack();

        } else if(result.equals("Lose")){
            labelInfo.setText("You cannot attack");
            // Enemy Attack
            enemyAttack();
            System.out.println("Enemy Attack");
        }
    }

    protected void characterAttack(){

    }
    protected void enemyAttack(){
        int enemyDamage = enemyComps.get(0).getDamage();
        int randomNum = 0;
        double random = Math.random();
        if (random <= 0.333333333333) {
             randomNum = 1;
        } else if (random <= 0.6666666666) {
             randomNum = 2;
        }

        int playerHP = characterComps.get(randomNum).getCurrentHp();
        int playerShield = characterComps.get(randomNum).getCurrentShield();
        enemyDamage = enemyDamage - playerShield;
        if(enemyDamage >= 0) {
            playerHP = playerHP - enemyDamage;
        }
        characterComps.get(randomNum).setCurrentHp(playerHP);
        characterComps.get(randomNum).setCurrentShield(playerShield);
    }
    public static void main(String[] args) {
        launch(args);
    }

}



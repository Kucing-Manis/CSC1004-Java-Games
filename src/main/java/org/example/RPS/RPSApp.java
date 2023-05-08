package org.example.RPS;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.CursorInfo;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import java.util.concurrent.ThreadLocalRandom;

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
    private Entity finishBattle, highlight, bgLabelInfo, bgLabelStage;
//    private CharacterComponent heroComp, wizardComp, healerComp, guardComp, fighterComp;
    private ArrayList<CharacterComponent> characterDataComps = new ArrayList<>();
    private ArrayList<CharacterComponent> characterComps = new ArrayList<>();
    private ArrayList<CharacterComponent> enemyComps = new ArrayList<>();
    private CharacterComponent actingCharacter;

    private Pane pane1,pane2,pane3,paneEnemy;
    private int enemyID = 0;
    private int selectedBox = 0;
    private int stage = 1;
    private int round = 1;
    private Text labelStage, labelInfo;
    private Button nextAction;
    private Boolean isP1Dead, isP2Dead, isP3Dead;
    private Boolean isBoss;
    private Boolean isGuard = false;
    private Boolean isDraw = false;
    private Timeline timeLineBattle;

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
    }

    @Override
    protected void initGame() {
        // Set Background
        getGameWorld().addEntityFactory(new GameEntityFactory());
        spawn("background");

        initVariables();
        initStage();

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

        //Finish Battle Click Function
        finishBattle.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, a -> {
            finishBattle.setVisible(false);
            characterAttack();
            // Delay
            Timeline delay = new Timeline(
                    new KeyFrame(Duration.seconds(2)
                    ));
            delay.setOnFinished(e -> {
                timeLineBattle.play();
            });

            // If Win or Draw
            if (isDraw){
                Timeline delayEnemyAttack = new Timeline(
                        new KeyFrame(Duration.seconds(2)
                        ));
                delayEnemyAttack.setOnFinished(e -> {
                        enemyAttack();
                        delay.play();
                });
                delayEnemyAttack.play();
            } else {
                delay.play();
            }

        });

        //Players Click Function (To Select Skill)
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
    }

    protected void initStage(){
        labelStage.setText("Stage " + stage + " Round " + round);
        initCharacter();
        initEnemy();
        spawnBoxPanels();
        addUINode(pane1);
        addUINode(pane2);
        addUINode(pane3);
        addUINode(paneEnemy);
        actingCharacter = characterComps.get(0);
        startRPS();
    }

    protected void nextStage(){
        labelStage.setText("Stage " + stage + " Round " + round);
        for (int i = 0; i < enemyComps.size(); i++) {
            addStats(enemyComps.get(i));
        }

        if(!isP1Dead){
            addStats(characterComps.get(0));
        }
        if(!isP2Dead){
            addStats(characterComps.get(1));
        }
        if(!isP3Dead){
            addStats(characterComps.get(2));
        }
        spawnBoxPanels();
        spawnEnemy();

        addUINode(pane1);
        addUINode(pane2);
        addUINode(pane3);
        addUINode(paneEnemy);
    }

    protected void initVariables(){
        finishBattle = FXGL.entityBuilder().at(800, 600).view(new Rectangle(100, 100, Color.BLACK)).buildAndAttach();
        finishBattle.setVisible(false);

        timeLineBattle = new Timeline(
                new KeyFrame(Duration.seconds(0.5)
                ));

        highlight = FXGL.entityBuilder().view(new Rectangle(194, 149, Color.WHITE)).buildAndAttach();
        highlight.setVisible(false);
        highlight.setOpacity(0.8);
        highlight.setPosition(3, 100 + 180*selectedBox - 12);

        bgLabelStage = FXGL.entityBuilder().at(getAppWidth() / 2.0 - 195, getAppHeight() - 685).view(new Rectangle(390, 70, Color.WHITE)).buildAndAttach();
        bgLabelStage.setOpacity(0.65);

        labelStage = new Text();
        labelStage.setFill(Color.BLACK);
        labelStage.setFont(new Font(50));
        labelStage.setLayoutX(getAppWidth() / 2.0 - 180);
        labelStage.setLayoutY(getAppHeight() - 635);

        Pane pane = new Pane(labelStage);
        addUINode(pane);

        bgLabelInfo = FXGL.entityBuilder().at(getAppWidth() / 2.0 - 205, getAppHeight() - 595).view(new Rectangle(410, 45, Color.WHITE)).buildAndAttach();
        bgLabelInfo.setOpacity(0.65);
        bgLabelInfo.setVisible(false);

        labelInfo = new Text();;
        labelInfo.setFill(Color.BLACK);
        labelInfo.setFont(new Font(25));
        labelInfo.setLayoutX(getAppWidth() / 2.0 - 180);
        labelInfo.setLayoutY(getAppHeight() - 560);

        rock = FXGL.entityBuilder().at(200, 600).view("ui/Rock.png").buildAndAttach();
        paper = FXGL.entityBuilder().at(400, 600).view("ui/Paper.jpeg").buildAndAttach();
        scissor = FXGL.entityBuilder().at(600, 600).view("ui/Scissor.png").buildAndAttach();
        rock.setScaleX(0.27);
        rock.setScaleY(0.27);
        paper.setScaleX(0.27);
        paper.setScaleY(0.27);
        scissor.setScaleX(0.27);
        scissor.setScaleY(0.27);
    }

    protected void spawnBoxPanels(){
        pane1 = boxPanel(characterComps.get(0), 0, true);
        pane2 = boxPanel(characterComps.get(1), 1, true);
        pane3 = boxPanel(characterComps.get(2), 2, true);
    }

    protected void initCharacter(){
        isP1Dead = false;
        isP2Dead = false;
        isP3Dead = false;

        // Sprite Data
        SpriteData spriteHero = new SpriteData("characters/Hero.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteWizard = new SpriteData("characters/Wizard.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteHealer = new SpriteData("characters/Healer.png", 6,64, 64, 0, 9, 1);
        SpriteData spriteGuard = new SpriteData("characters/Guard.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteFighter = new SpriteData("characters/Fighter.png", 6, 64, 64, 0, 9, 1);

        // Input Character into Data
        characterDataComps.add(new CharacterComponent("Hero", "Justice Attack", "", 1,10,1,2, spriteHero));
        characterDataComps.add(new CharacterComponent("Wizard", "Fireball", "", 1,5,1,3, spriteWizard));
        characterDataComps.add(new CharacterComponent("Healer", "Heal", "",1,6,1,1, spriteHealer));
        characterDataComps.add(new CharacterComponent("Guard", "Guard Team", "", 1,15,1,1, spriteGuard));
        characterDataComps.add(new CharacterComponent("Fighter", "Pierce", "", 1,7,1,3, spriteFighter));

        // Select Random Character
        characterComps.add(characterDataComps.get(0));
        characterComps.add(characterDataComps.get(1));
        characterComps.add(characterDataComps.get(2));

//        p1 = FXGL.entityBuilder()
//                .at(185, 500)
//                .with(new AnimationComponent(new SpriteData("characters/Hero.png", 6, 64, 64, 0, 9, 1)))
//                .buildAndAttach();
        p1 = FXGL.entityBuilder()
                .at(295, 460)
                .with(new AnimationComponent(characterComps.get(0).getSpriteData(), true))
                .buildAndAttach();
        p2 = FXGL.entityBuilder()
                .at(250, 400)
                .with(new AnimationComponent(characterComps.get(1).getSpriteData(), true))
                .buildAndAttach();
        p3 = FXGL.entityBuilder()
                .at(250, 520)
                .with(new AnimationComponent(characterComps.get(2).getSpriteData(), true))
                .buildAndAttach();
    }

    protected void initEnemy(){
        isBoss = false;

        // Sprite Data
        SpriteData spriteFighter = new SpriteData("characters/Fighter.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteHero = new SpriteData("characters/Hero.png", 6, 64, 64, 0, 9, 1);

        enemyComps.add(new CharacterComponent("Noob Fighter", "Weak Attack", "", 1,5,1,1, spriteFighter));
        enemyComps.add(new CharacterComponent("Evil Fighter", "Big DAMAGE", "", 1,15,6,3, spriteFighter));
        enemyComps.add(new CharacterComponent("Evil Hero", "Evil Attack", "", 1,10,3,2, spriteHero));
        enemyComps.add(new CharacterComponent("Super Evil Hero", "Super Evil", "", 1,10,3,3, spriteHero));

        enemy = FXGL.entityBuilder()
                .at(830, 460)
                .with(new AnimationComponent(enemyComps.get(enemyID).getSpriteData(), false))
                .buildAndAttach();
    }

    protected void spawnEnemy(){
        if(stage % 5 == 0){
            isBoss = true;
        } else {
            isBoss = false;
        }

        enemyID = randomNumGenerator(0, 3);
        enemy = FXGL.entityBuilder()
                .at(830, 460)
                .with(new AnimationComponent(enemyComps.get(enemyID).getSpriteData(), false))
                .buildAndAttach();

        paneEnemy = boxPanel(enemyComps.get(enemyID), 0, false);
    }
    protected void startRPS() {
        rock.setVisible(true);
        paper.setVisible(true);
        scissor.setVisible(true);
        highlight.setVisible(false);
        finishBattle.setVisible(false);
        bgLabelInfo.setVisible(false);
        isDraw = false;
        labelStage.setText("Stage " + stage + " Round " + round);
    }

    protected void addStats(CharacterComponent CC){
        int CC_Level = CC.getLevel() + 1;
        int CC_MaxHP = CC.getMaxHp();
        int CC_CurrentHP = CC.getCurrentHp();
        int CC_MaxShield = CC.getMaxShield();
        int CC_Damage = CC.getDamage();

        // Level
        CC.setLevel(CC_Level);
        int hpBoost = 0;
        int shieldBoost = 0;
        int damageBoost = 0;
        if(CC_Level % 3 == 0){
            hpBoost = 2;
        }
        if(CC_Level % 5 == 0){
            shieldBoost = 1;
        }
        if(CC_Level % 10 == 0){
            damageBoost = 1;
        }


        // Hp
        int randomHP = randomNumGenerator(0, 4);
        CC.setMaxHp(CC_MaxHP + randomHP + hpBoost);
        CC.setCurrentHp(CC_CurrentHP + randomHP + hpBoost);
        CC.getHp().setValue(CC_CurrentHP + randomHP + hpBoost);

        // Shield
        int randomShield = randomNumGenerator(0, 2);
        CC.setMaxShield(CC_MaxShield + randomShield + shieldBoost);
        CC.getShield().setValue(CC_MaxShield + randomShield + shieldBoost);

        // Damage
        double randomDamage = Math.random();
        if(randomDamage <= 0.00005){
            CC.setDamage(CC_Damage + 10 + damageBoost);
        } else if (randomDamage <= 0.1) {
            CC.setDamage(CC_Damage + 2 + damageBoost);
        }else if (randomDamage <= 0.4) {
            CC.setDamage(CC_Damage + 1 + damageBoost);
        } else {
            CC.setDamage(CC_Damage + damageBoost);
        }
    }
    protected int randomNumGenerator(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    protected Pane boxPanel(CharacterComponent CC, int num, boolean isPlayer){
        Rectangle bgRect = new Rectangle(190, 150);
        int x = 5;
        int y = 100 + (180*num);
        if(!isPlayer){
            x = getAppWidth() - 200;
        }
        bgRect.setLayoutX(x);
        bgRect.setLayoutY(y - 10);
        bgRect.setOpacity(0.8);
        Text levelName = getUIFactoryService().newText(CC.getName(), Color.WHITE, 18);
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

//        Button clickMe = new Button();
//        clickMe.setId(num + "");
//        clickMe.setText("Click Me");
//        clickMe.setOnAction(event -> {
//            actingCharacter = CC;
//            selectedBox = num;
//            highlight.setPosition(3, 100 + 180*selectedBox - 12);
//            System.out.println(actingCharacter.getName() + "   " + selectedBox);
//        });

        VBox box = new VBox(6);//登录VBOX
        box.setAlignment(Pos.CENTER_LEFT);
        box.setLayoutX(x+8);
        box.setLayoutY(y);
        box.setVisible(true);

        box.getChildren().addAll(
                levelName,
                boxHP,
                boxShield,
                skill);

        return new Pane(bgRect, box);

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
            ttRock.setToY(getAppHeight() / 2.0 + 30);
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
            ttPaper.setToY(getAppHeight() / 2.0 - 13 + 30);
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
            ttScissor.setToY(getAppHeight() / 2.0 + 30);
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
        Timeline timeLine = new Timeline(
                new KeyFrame(Duration.seconds(5.1)
                ));
        timeLine.setOnFinished(e -> {
            removeUINode(pane);
        });
        timeLine.play();
    }

    protected void enemyTransition(String enemy, String input) {
        // Set Text
        Text result = new Text();
        result.setText(input);
        result.setFill(Color.BLACK);
        result.setFont(new Font(70));
        result.setLayoutX(getAppWidth() / 2.0 - 90);
        result.setLayoutY(getAppHeight() / 2.0 - 150);
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
            ttRock.setToY(getAppHeight() / 2.0 + 30);
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
            ttPaper.setToY(getAppHeight() / 2.0 - 13 + 30);
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
            ttScissor.setToY(getAppHeight() / 2.0 + 30);
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
        Timeline timeLine = new Timeline(
                new KeyFrame(Duration.seconds(3.1)
                ));
        timeLine.setOnFinished(e -> {
            removeUINode(pane);
            battleTime(input);
        });
        timeLine.play();
    }

    protected void battleTime(String result) {
        labelInfo.setVisible(true);
        bgLabelInfo.setVisible(true);
        Pane pane = new Pane(labelInfo);
        addUINode(pane);

        timeLineBattle.setOnFinished(e -> {
            removeUINode(pane);
            round += 1;
            startRPS();
        });

        // Check Result Condition
        if(result.equals("Win")){
            // When Win, Player attack, Enemy do not attack
            System.out.println("WIN");
            labelInfo.setText("Pick your skills from the character");
            isDraw = false;
            highlight.setVisible(true);
            finishBattle.setVisible(true);
        } else if(result.equals("Draw")){
            // When Draw, Player attack, Enemy attack
            System.out.println("DRAW");
            labelInfo.setText("Pick your skills from the character");
            isDraw = true;
            highlight.setVisible(true);
            finishBattle.setVisible(true);
        } else if(result.equals("Lose")){
            // When Lose, Player attack, Enemy do not attack
            System.out.println("LOSE");
            labelInfo.setText("You lose, you cannot attack");

            // Delay
            Timeline delay = new Timeline(
                    new KeyFrame(Duration.seconds(2)
                    ));
            delay.setOnFinished(e -> {
                enemyAttack();
                timeLineBattle.play();
            });
            delay.play();

        }
    }

    protected void characterAttack(){
        boolean isAttack = false;
        CharacterComponent enemyCC = enemyComps.get(0);
        int enemyHp = enemyCC.getCurrentHp();
        int enemyShield = enemyCC.getCurrentShield();
        int damage = actingCharacter.getDamage();
        int damageAfterShield = 0;
        int p1Hp, p2Hp, p3Hp, p1MaxHp, p2MaxHp, p3MaxHp;
        String skillName = actingCharacter.getSkillName();

        // Check Which Skill
        if(skillName.equals("Justice")){
            isAttack = true;
            damage = damage * randomNumGenerator(1, 2);
            damageAfterShield = damage - enemyShield;
            if(damageAfterShield >= 0){
                enemyCC.getShield().damage(enemyShield);
                enemyCC.getHp().damage(damageAfterShield);
                enemyHp = enemyHp - damageAfterShield;
                enemyShield = 0;
            } else {
                enemyShield = enemyShield - damage;
                enemyCC.getShield().damage(damage);
            }
            labelInfo.setText("Deal " + damage + " damage to Enemy" );
        } else if (skillName.equals("Fireball")) {
            isAttack = true;
            damage = damage * randomNumGenerator(0, 3);
            damageAfterShield = damage - enemyShield;
            if(damageAfterShield >= 0){
                enemyCC.getShield().damage(enemyShield);
                enemyCC.getHp().damage(damageAfterShield);
                enemyHp = enemyHp - damageAfterShield;
                enemyShield = 0;
            } else {
                enemyShield = enemyShield - damage;
                enemyCC.getShield().damage(damage);
            }
            labelInfo.setText("Deal " + damage + " damage to Enemy" );
        } else if (skillName.equals("Pierce")) {
            isAttack = true;
            enemyHp = enemyHp - damage;
            enemyCC.getHp().damage(damage);
            labelInfo.setText("Deal " + damage + " true damage to Enemy" );
        } else if (skillName.equals("Heal")) {
            p1Hp = characterComps.get(0).getCurrentHp();
            p2Hp = characterComps.get(1).getCurrentHp();
            p3Hp = characterComps.get(2).getCurrentHp();
            p1MaxHp = characterComps.get(0).getMaxHp();
            p2MaxHp = characterComps.get(1).getMaxHp();
            p3MaxHp = characterComps.get(2).getMaxHp();
            if(p1MaxHp > p1Hp + damage){
                characterComps.get(0).getHp().setValue(p1Hp + damage);
                characterComps.get(0).setCurrentHp(p1Hp + damage);
            } else {
                characterComps.get(0).getHp().setValue(p1MaxHp);
                characterComps.get(0).setCurrentHp(p1MaxHp);
            }
            if(p2MaxHp > p2Hp + damage){
                characterComps.get(1).getHp().setValue(p2Hp + damage);
                characterComps.get(1).setCurrentHp(p2Hp + damage);
            } else {
                characterComps.get(1).getHp().setValue(p2MaxHp);
                characterComps.get(1).setCurrentHp(p2MaxHp);
            }
            if(p3MaxHp > p3Hp + damage){
                characterComps.get(2).getHp().setValue(p3Hp + damage);
                characterComps.get(2).setCurrentHp(p3Hp + damage);
            } else {
                characterComps.get(2).getHp().setValue(p3MaxHp);
                characterComps.get(2).setCurrentHp(p3MaxHp);
            }
            labelInfo.setText("Heal " + damage + " Hp to all" );
        } else if(skillName.equals("Guard Team")){
            isAttack = true;
            isGuard = true;
            damage = damage * randomNumGenerator(0, 1);
            damageAfterShield = damage - enemyShield;
            if(damageAfterShield >= 0){
                enemyCC.getShield().damage(enemyShield);
                enemyCC.getHp().damage(damageAfterShield);
                enemyHp = enemyHp - damageAfterShield;
                enemyShield = 0;
            } else {
                enemyShield = enemyShield - damage;
                enemyCC.getShield().damage(damage);
            }
            labelInfo.setText("Deal " + damage + " damage to Enemy" );
        }
        if(isAttack){
            if(enemyHp <= 0){
                // Delay End Stage
                Timeline delayEndStage = new Timeline(
                        new KeyFrame(Duration.seconds(1.5)
                        ));
                delayEndStage.setOnFinished(e -> {
                    endStage();
                });

                // Delay
                Timeline delay = new Timeline(
                        new KeyFrame(Duration.seconds(1)
                        ));
                int finalRandomNum = 0;
                delay.setOnFinished(e -> {
                    labelInfo.setText("Enemy " + enemyCC.getName() + " die");
                    delayEndStage.play();
                });
                delay.play();
            } else {
                enemyCC.setCurrentHp(enemyHp);
                enemyCC.setCurrentShield(enemyShield);
            }
        }
        System.out.println("Success Character Attack");
    }

    protected void enemyAttack(){
        int enemyDamage = enemyComps.get(0).getDamage();
        if(isBoss){
            enemyDamage = enemyDamage * randomNumGenerator(1,2);
        }
        int tempDamage = enemyDamage;
        int randomNum = randomNumGenerator(0, 2);
        if(isGuard){
            randomNum = selectedBox;
        }

        //Check Character Alive or Dead
        if(randomNum == 0 && isP1Dead){
            randomNum = randomNumGenerator(1,2);
            if (randomNum == 1 && isP2Dead) {
                randomNum = 2;
            } else if (randomNum == 2 && isP3Dead) {
                randomNum = 1;
            }
        } else if (randomNum == 1 && isP2Dead) {
            if(randomNum == 0 && isP1Dead){
                randomNum = 2;
            } else {
                randomNum = 0;
            }
        } else if (randomNum == 2 && isP3Dead) {
            randomNum = randomNumGenerator(0,1);
            if (randomNum == 0 && isP1Dead) {
                randomNum = 1;
            } else if (randomNum == 1 && isP2Dead) {
                randomNum = 0;
            }
        }

        CharacterComponent CC = characterComps.get(randomNum);

        int playerHP = CC.getCurrentHp();
        int playerShield = CC.getCurrentShield();
        enemyDamage = enemyDamage - playerShield;
        CC.getShield().damage(enemyDamage - (enemyDamage-playerShield));
        if(enemyDamage > 0) {
            playerHP = playerHP - enemyDamage;
            playerShield = 0;
            CC.getHp().damage(enemyDamage);
        } else {
            playerShield = playerShield - (enemyDamage + playerShield);
        }
        labelInfo.setText("Enemy deal " + tempDamage + " damage to " + CC.getName());
        System.out.println("Enemy deal " + tempDamage + " damage to " + CC.getName());

        if(playerHP <= 0){
            // Delay
            Timeline delay = new Timeline(
                    new KeyFrame(Duration.seconds(0.8)
                    ));
            int finalRandomNum = randomNum;
            delay.setOnFinished(e -> {
                labelInfo.setText("Your " + CC.getName() + " die");
                characterDead(CC, finalRandomNum);
            });
            delay.play();
        } else {
            CC.setCurrentHp(playerHP);
            CC.setCurrentShield(playerShield);
        }
        System.out.println("Success Enemy Attack");
    }
    protected void characterDead(CharacterComponent CC, int num){
        if(CC.getSkillName().equals("Guard Team")){
            isGuard = false;
        }
        if(num == 0){
            isP1Dead = true;
            p1.removeFromWorld();
            removeUINode(pane1);
        } else if (num == 1) {
            isP2Dead = true;
            p2.removeFromWorld();
            removeUINode(pane2);
        } else {
            // num = 2
            isP3Dead = true;
            p3.removeFromWorld();
            removeUINode(pane3);
        }
    }
    protected void endStage(){
        stage = stage + 1;
        round = 1;

        // Remove Box Panels
        removeUINode(pane1);
        removeUINode(pane2);
        removeUINode(pane3);
        removeUINode(paneEnemy);

        // Remove Enemy
        enemy.removeFromWorld();

        endStageScene();
        nextStage();
    }

    protected void GameOver(){

    }

    protected void endStageScene() {
        Rectangle rect1 = new Rectangle(getAppWidth(), getAppHeight() / 2.0, Color.web("#333333"));
        Rectangle rect2 = new Rectangle(getAppWidth(), getAppHeight() / 2.0, Color.web("#333333"));
        rect2.setLayoutY(getAppHeight() / 2.0);
        Text stageText = new Text("STAGE " + stage);
        stageText.setFill(Color.WHITE);
        stageText.setFont(new Font(35));
        stageText.setLayoutX(getAppWidth() / 2.0 - 80);
        stageText.setLayoutY(getAppHeight() / 2.0 - 5);
        Pane pane = new Pane(rect1, rect2, stageText);

        addUINode(pane);

        Timeline rectTranslation = new Timeline(
                new KeyFrame(Duration.seconds(1.2),
                        new KeyValue(rect1.translateYProperty(), -getAppHeight() / 2.0),
                        new KeyValue(rect2.translateYProperty(), getAppHeight() / 2.0)
                ));
        rectTranslation.setOnFinished(e -> removeUINode(pane));

        PauseTransition pt = new PauseTransition(Duration.seconds(1.5));
        pt.setOnFinished(e -> {
            stageText.setVisible(false);
            rectTranslation.play();
        });
        pt.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



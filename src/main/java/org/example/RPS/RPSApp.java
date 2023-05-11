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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.RPS.animation.AnimationComponent;
import org.example.RPS.animation.SpriteData;
import org.example.RPS.component.CharacterComponent;
import org.example.RPS.db.JdbcUtils;
import org.example.RPS.view.GameMainMenu;
import org.example.RPS.view.GameSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.almasb.fxgl.app.GameApplication.launch;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;

public class RPSApp extends GameApplication {
    GameSettings setting = new GameSettings();
    private static String userName;
    private static int highScore;
    private Entity p1, p2, p3, enemy;
    private Entity rock, paper, scissor;
    private Entity finishBattle, highlight, bgLabelInfo, bgLabelStage, gameOver, cheat, noCheat;
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
    private Boolean isP1Dead, isP2Dead, isP3Dead, isEnemyDead;
    private Boolean isBoss;
    private Boolean isGuard = false;
    private Boolean isDraw = false;
    private Boolean isCheatingMode = false;
    private Timeline timeLineBattle;
    private MediaPlayer gameSound;

    @Override
    protected void initSettings(com.almasb.fxgl.app.GameSettings settings) {
        setting.initSettings(settings); // Initiate the settings from view/GameSettings
        settings.setTitle("Rock Paper Scissor RPG");
        settings.setVersion("0.1");
        settings.getCSSList().add("RPSApp.css");
        settings.setDefaultCursor(new CursorInfo("ui/Cursor.png", 0, 0)); // Set the cursor into ui/Cursor.png
    }

    @Override
    protected void initInput() {
        // Not Used
    }

    // When starting the game, initGame() function run first time
    @Override
    protected void initGame() {
        // Set Background
        getGameWorld().addEntityFactory(new GameEntityFactory());
        spawn("background");

        // Play Background Music
        Media media = new Media(getClass().getResource("/assets/sounds/gameMusic.wav").toExternalForm());
        gameSound = new MediaPlayer(media);
        gameSound.play();
        gameSound.setCycleCount(MediaPlayer.INDEFINITE); // Cause it loop infinitely
        gameSound.setVolume(0.5);

        // Call function
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

        // Set CheatMode Click Function
        cheat.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            isCheatingMode = true;
            cheat.setVisible(false);
            noCheat.setVisible(true);
            System.out.println(isCheatingMode);
        });
        noCheat.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            isCheatingMode = false;
            cheat.setVisible(true);
            noCheat.setVisible(false);
            System.out.println(isCheatingMode);
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

            // If Win or Draw (If draw enemy attack, if win, enemy do not attack)
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

        //Players Click Function (To select skill, Click the character)
        p1.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            actingCharacter = characterComps.get(0);
            selectedBox = 0;
            highlight.setPosition(3, 100 + 180*selectedBox - 12);
        });
        p2.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            actingCharacter = characterComps.get(1);
            selectedBox = 1;
            highlight.setPosition(3, 100 + 180*selectedBox - 12);
        });
        p3.getViewComponent().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            actingCharacter = characterComps.get(2);
            selectedBox = 2;
            highlight.setPosition(3, 100 + 180*selectedBox - 12);
        });
    }

    // Set the data of username and highscore (GameMainMenu will call this function and transfer the data to RPSApp)
    public static void setData(String NAME, int SCORE){
        userName = NAME;
        highScore = SCORE;
    }

    // Initialize the first stage
    protected void initStage(){
        isCheatingMode = false;
        labelStage.setText("Stage " + stage + " Round " + round); // Set the labelStage
        // Spawn or Initialize characters, enemy, and box panels
        initCharacter();
        initEnemy();
        spawnBoxPanels();

        // Add UI Node of the box panels
        addUINode(pane1);
        addUINode(pane2);
        addUINode(pane3);
        addUINode(paneEnemy);

        // Set actingCharacter and selectedBox
        actingCharacter = characterComps.get(0);
        selectedBox = 0;

        // Start Rock Paper Scissor
        startRPS();
    }

    // Next Stage (after defeating or killing the enemy)
    protected void nextStage(){
        labelStage.setText("Stage " + stage + " Round " + round);
        for (int i = 0; i < enemyComps.size(); i++) {
            addStats(enemyComps.get(i), true);
        }

        // Check if p1 or p2 or p3 is dead. If dead, then no addstats and no addUINode
        if(!isP1Dead){
            addStats(characterComps.get(0), false);
        }
        if(!isP2Dead){
            addStats(characterComps.get(1), false);
        }
        if(!isP3Dead){
            addStats(characterComps.get(2), false);
        }
        spawnBoxPanels();
        if(!isP1Dead){
            addUINode(pane1);
        }
        if(!isP2Dead){
            addUINode(pane2);
        }
        if(!isP3Dead){
            addUINode(pane3);
        }

        // Spawn new random enemy
        spawnEnemy();
        addUINode(paneEnemy);

        // Deactivating the Guard Skill
        isGuard = false;
    }

    // Initialize the variables
    protected void initVariables(){
        // GameOver Image
        gameOver = FXGL.entityBuilder().at(320, 300).view("ui/GameOver.png").buildAndAttach();
        gameOver.setScaleX(5);
        gameOver.setScaleY(5);
        gameOver.setVisible(false);

        // Image acting as a button to end the battle and startRPS() function
        finishBattle = FXGL.entityBuilder().at(800, 600).view("ui/Attack.png").buildAndAttach();
        finishBattle.setScaleX(0.05);
        finishBattle.setScaleY(0.05);
        finishBattle.setVisible(false);

        // Image acting as a button to start or enable cheating mode
        cheat = FXGL.entityBuilder().at(13, 610).view("ui/Cheat.png").buildAndAttach();
        cheat.setScaleX(0.65);
        cheat.setScaleY(0.65);
        cheat.setVisible(true);

        // Image acting as a button to stop or disable cheating mode
        noCheat = FXGL.entityBuilder().at(43, 630).view("ui/NoCheat.png").buildAndAttach();
        noCheat.setScaleX(0.1);
        noCheat.setScaleY(0.1);
        noCheat.setVisible(false);

        // Time delay for starting the startRPS() function
        timeLineBattle = new Timeline(
                new KeyFrame(Duration.seconds(1.5)
                ));

        // Highlight the selectedBox
        highlight = FXGL.entityBuilder().view(new Rectangle(194, 154, Color.WHITE)).buildAndAttach();
        highlight.setVisible(false);
        highlight.setOpacity(0.8);
        highlight.setPosition(3, 100 + 180*selectedBox - 12);

        // White Background for the labelStage
        bgLabelStage = FXGL.entityBuilder().at(getAppWidth() / 2.0 - 210, getAppHeight() - 685).view(new Rectangle(420, 70, Color.WHITE)).buildAndAttach();
        bgLabelStage.setOpacity(0.65);

        // Set the Stage and Round text
        labelStage = new Text();
        labelStage.setFill(Color.BLACK);
        labelStage.setFont(new Font(50));
        labelStage.setLayoutX(getAppWidth() / 2.0 - 190);
        labelStage.setLayoutY(getAppHeight() - 633);

        Pane pane = new Pane(labelStage);
        addUINode(pane);

        // White Background for the labelInfo
        bgLabelInfo = FXGL.entityBuilder().at(getAppWidth() / 2.0 - 205, getAppHeight() - 595).view(new Rectangle(410, 45, Color.WHITE)).buildAndAttach();
        bgLabelInfo.setOpacity(0.65);
        bgLabelInfo.setVisible(false);

        // Set the information text
        labelInfo = new Text();;
        labelInfo.setFill(Color.BLACK);
        labelInfo.setFont(new Font(25));
        labelInfo.setLayoutX(getAppWidth() / 2.0 - 180);
        labelInfo.setLayoutY(getAppHeight() - 560);

        // Set the image for the Rock, Paper, and Scissor
        rock = FXGL.entityBuilder().at(230, 600).view("ui/Rock.png").buildAndAttach();
        paper = FXGL.entityBuilder().at(430, 600).view("ui/Paper.jpeg").buildAndAttach();
        scissor = FXGL.entityBuilder().at(630, 600).view("ui/Scissor.png").buildAndAttach();
        rock.setScaleX(0.27);
        rock.setScaleY(0.27);
        paper.setScaleX(0.27);
        paper.setScaleY(0.27);
        scissor.setScaleX(0.27);
        scissor.setScaleY(0.27);
    }

    // Spawn box panels
    protected void spawnBoxPanels(){
        pane1 = boxPanel(characterComps.get(0), 0, true);
        pane2 = boxPanel(characterComps.get(1), 1, true);
        pane3 = boxPanel(characterComps.get(2), 2, true);
    }

    // Initialize the character (At initStage() function)
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
        characterDataComps.add(new CharacterComponent("Hero", "Justice", "", 1,10,3,2, spriteHero));
        characterDataComps.add(new CharacterComponent("Wizard", "Fireball", "", 1,5,1,3, spriteWizard));
        characterDataComps.add(new CharacterComponent("Healer", "Heal", "",1,6,1,1, spriteHealer));
        characterDataComps.add(new CharacterComponent("Guard", "Guard", "", 1,15,4,1, spriteGuard));
        characterDataComps.add(new CharacterComponent("Fighter", "Pierce", "", 1,7,2,3, spriteFighter));

        // Select Random Character
        int randomNum = randomNumGenerator(0,2);
        if(randomNum == 0){
            characterComps.add(characterDataComps.get(3));
            characterComps.add(characterDataComps.get(1));
            characterComps.add(characterDataComps.get(2));
        } else if(randomNum == 1){
            characterComps.add(characterDataComps.get(3));
            characterComps.add(characterDataComps.get(1));
            characterComps.add(characterDataComps.get(4));
        } else if(randomNum == 2){
            characterComps.add(characterDataComps.get(0));
            characterComps.add(characterDataComps.get(4));
            characterComps.add(characterDataComps.get(2));
        } else {
            characterComps.add(characterDataComps.get(0));
            characterComps.add(characterDataComps.get(1));
            characterComps.add(characterDataComps.get(2));
        }

        // Build the player
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

    // Initialize the enemy (Only call once at initStage() function)
    protected void initEnemy(){
        isBoss = false;
        isEnemyDead = false;

        // Sprite Data
        SpriteData spriteFighter = new SpriteData("characters/Fighter.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteHero = new SpriteData("characters/Hero.png", 6, 64, 64, 0, 9, 1);
        SpriteData spriteGuard = new SpriteData("characters/Guard.png", 6, 64, 64, 0, 9, 1);

        // Input enemy into Data
        enemyComps.add(new CharacterComponent("Noob Fighter", "Weak Attack", "", 1,5,1,1, spriteFighter));
        enemyComps.add(new CharacterComponent("Evil Fighter", "Big DAMAGE", "", 1,9,3,3, spriteFighter));
        enemyComps.add(new CharacterComponent("Evil Hero", "Evil Attack", "", 1,8,2,2, spriteHero));
        enemyComps.add(new CharacterComponent("Super Evil Hero", "Super Attack", "", 1,12,4,2, spriteHero));
        enemyComps.add(new CharacterComponent("Shield Maniac", "Shield Bash", "", 1,2,10,2, spriteGuard));

        // Build enemy
        enemy = FXGL.entityBuilder()
                .at(830, 460)
                .with(new AnimationComponent(enemyComps.get(enemyID).getSpriteData(), false))
                .buildAndAttach();
        paneEnemy = boxPanel(enemyComps.get(enemyID), 0, false);
    }

    // Spawn enemy (At nextStage() function)
    protected void spawnEnemy(){
        // Make enemy boss every 5 level (Can deal 1-2 times damage)
        if(stage % 5 == 0){
            isBoss = true;
        } else {
            isBoss = false;
        }
        // Set the isEnemyDead false
        isEnemyDead = false;

        // Spawn random enemy
        enemyID = randomNumGenerator(0, 4);
        enemy = FXGL.entityBuilder()
                .at(830, 460)
                .with(new AnimationComponent(enemyComps.get(enemyID).getSpriteData(), false))
                .buildAndAttach();

        paneEnemy = boxPanel(enemyComps.get(enemyID), 0, false);
    }

    // Start the Rock Paper Scissor part
    protected void startRPS() {
        // Check if p1, p2, p3 dead or alive. If dead, it will be game over
        if(isP1Dead && isP2Dead && isP3Dead){
            GameOver();
        } else {
            // Set visible, boolean, and labelStage text
            rock.setVisible(true);
            paper.setVisible(true);
            scissor.setVisible(true);
            highlight.setVisible(false);
            finishBattle.setVisible(false);
            bgLabelInfo.setVisible(false);
            isDraw = false;
            labelStage.setText("Stage " + stage + " Round " + round);
        }
    }

    // Add character and enemy stats (At nextStage() function)
    protected void addStats(CharacterComponent CC, boolean isEnemy){
        int CC_Level = CC.getLevel() + 1;
        int CC_MaxHP = CC.getMaxHp();
        int CC_CurrentHP = CC.getCurrentHp();
        int CC_MaxShield = CC.getMaxShield();
        int CC_Damage = CC.getDamage();

        if(isEnemy){
            CC_CurrentHP = CC_MaxHP;
        }

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
        CC.getHp().setMaxValue(CC_MaxHP + randomHP + hpBoost);

        // Shield
        int randomShield = randomNumGenerator(0, 2);
        CC.setMaxShield(CC_MaxShield + randomShield + shieldBoost);
        CC.setCurrentShield(CC_MaxShield + randomShield + shieldBoost);
        CC.getShield().setValue(CC_MaxShield + randomShield + shieldBoost);
        CC.getShield().setMaxValue(CC_MaxShield + randomShield + shieldBoost);

        // Damage
        double randomDamage = Math.random();
        if(randomDamage <= 0.00005){
            CC.setDamage(CC_Damage + 10 + damageBoost); // For fun
        } else if (randomDamage <= 0.1) {
            CC.setDamage(CC_Damage + 2 + damageBoost);
        }else if (randomDamage <= 0.4) {
            CC.setDamage(CC_Damage + 1 + damageBoost);
        } else {
            CC.setDamage(CC_Damage + damageBoost);
        }
    }

    // randomNumGenerator to generate pseudo int random number between the minimal and the maximal
    protected int randomNumGenerator(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // Create the box panel
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
        Text skill = getUIFactoryService().newText( "Skill: " + CC.getSkillName() + " | Damage: " + CC.getDamage(), Color.WHITE, 14);
        String tempName;
        if(isPlayer){
            tempName = "Skill: " + CC.getSkillName() + " | Damage: " + CC.getDamage();
            skill.setText(tempName);
        } else {
            tempName = "Skill: " + CC.getSkillName() + " (" + CC.getDamage() + ")";
            skill.setText(tempName);
        }

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

    // Enemy Random Pick RPS
    protected String enemyPick(String playerPick){
        double random = Math.random();
        String enemyPick = "rock";

        // If not cheating mode, 33% random pick
        if(!isCheatingMode){
            if (random <= 0.333333333333) {
                enemyPick = "paper";
            } else if (random <= 0.6666666666) {
                enemyPick = "scissor";
            }
        } else {
            // If cheating mode, 50% Win, 30% Draw, 20% Lose
            if(playerPick.equals("rock")){
                enemyPick = "scissor";
                if (random <= 0.2) {
                    enemyPick = "paper";
                } else if (random <= 0.5) {
                    enemyPick = "rock";
                }
            } else if (playerPick.equals("paper")) {
                enemyPick = "rock";
                if (random <= 0.2) {
                    enemyPick = "scissor";
                } else if (random <= 0.5) {
                    enemyPick = "paper";
                }
            } else {
                // When player pick scissor
                enemyPick = "paper";
                if (random <= 0.2) {
                    enemyPick = "rock";
                } else if (random <= 0.5) {
                    enemyPick = "scissor";
                }
            }
        }
        return enemyPick;
    }

    // Check Win, Draw, or Lose in Rock Paper Scissor
    protected void calculateEnemyRPS(String playerPick) {
        String enemyPick = enemyPick(playerPick);

        // Check Player vs Enemy in RPS (Win or Draw or Lose)
        String resultRPS = "";
        if (playerPick.equals("rock")) {
            if (enemyPick.equals("rock")) {
                resultRPS = "Draw";
            } else if (enemyPick.equals("paper")) {
                resultRPS = "Lose";
            } else {
                resultRPS = "Win";
            }
        } else if (playerPick.equals("paper")) {
            if (enemyPick.equals("rock")) {
                resultRPS = "Win";
            } else if (enemyPick.equals("paper")) {
                resultRPS = "Draw";
            } else {
                resultRPS = "Lose";
            }
        } else {
            if (enemyPick.equals("rock")) {
                resultRPS = "Lose";
            } else if (enemyPick.equals("paper")) {
                resultRPS = "Win";
            } else {
                resultRPS = "Draw";
            }
        }
        // Go to TransitionRPS
        TransitionRPS(playerPick, enemyPick, resultRPS);
        rock.setVisible(false);
        paper.setVisible(false);
        scissor.setVisible(false);
    }

    // To transition the rock paper scissor image of the player
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
        // Check Player Pick and Transition
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

    // To transition the rock paper scissor image of the enemy
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
        // setScaleX negative to mirror from right to left
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
        // Check Enemy Pick and Transition
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
            battleTime(input); // Go to battleTime() function
        });
        timeLine.play();
    }

    // After Rock Paper Scissor, player and enemy can attack depending on the RPS result
    protected void battleTime(String result) {
        labelInfo.setVisible(true);
        bgLabelInfo.setVisible(true);
        Pane pane = new Pane(labelInfo);
        addUINode(pane);

        // Set timeLineBattle delay or timer
        timeLineBattle.setOnFinished(e -> {
            removeUINode(pane);
            round += 1;
            startRPS();
        });

        // Check Result Condition
        if(result.equals("Win")){
            // When Win, Player attack, Enemy do not attack
            labelInfo.setText("Pick your skills from the character");
            isDraw = false;
            highlight.setVisible(true);
            finishBattle.setVisible(true);
        } else if(result.equals("Draw")){
            // When Draw, Player attack, Enemy attack
            labelInfo.setText("Pick your skills from the character");
            isDraw = true;
            highlight.setVisible(true);
            finishBattle.setVisible(true);
        } else if(result.equals("Lose")){
            // When Lose, Player attack, Enemy do not attack
            labelInfo.setText("You LOSE! You cannot attack!");

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

    // Character Attack Function
    protected void characterAttack(){
        boolean isAttack = false;
        CharacterComponent enemyCC = enemyComps.get(enemyID);
        int enemyHp = enemyCC.getCurrentHp();
        int enemyShield = enemyCC.getCurrentShield();
        int damage = actingCharacter.getDamage();
        int damageAfterShield = 0;
        int p1Hp, p2Hp, p3Hp, p1MaxHp, p2MaxHp, p3MaxHp;
        String skillName = actingCharacter.getSkillName();

        // Check Which Skill (Check the player skill name)
        if(skillName.equals("Justice")){
            // Justice deal 1-2 times the player damage
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
            // Fireball deal 0-3 times the player damage
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
            // Pierce deal damage to the enemy hp (Enemy Shield is ignored)
            isAttack = true;
            enemyHp = enemyHp - damage;
            enemyCC.getHp().damage(damage);
            labelInfo.setText("Deal " + damage + " true damage to Enemy" );
        } else if (skillName.equals("Heal")) {
            // Heal set everyone hp to +(healer damage)
            p1Hp = characterComps.get(0).getCurrentHp();
            p2Hp = characterComps.get(1).getCurrentHp();
            p3Hp = characterComps.get(2).getCurrentHp();
            p1MaxHp = characterComps.get(0).getMaxHp();
            p2MaxHp = characterComps.get(1).getMaxHp();
            p3MaxHp = characterComps.get(2).getMaxHp();
            // Check if HP + damage will be above maxHP or not
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
        } else if(skillName.equals("Guard")){
            // Guard cause enemy to only attack guard and deal 0-1 times damage. If guard skill is used again, it will deactivate the skill.
            isAttack = true;
            if(!isGuard){
                isGuard = true;
            } else {
                isGuard = false;
            }
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
        // If the character attack, check if the enemy hp is 0 or below 0
        if(isAttack){
            if(enemyHp <= 0){
                // If below or equal to 0, enemy is dead and stage is clear. Hence, it will start endStage() function
                isEnemyDead = true;

                // Delay endStage() Function
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
                    FXGL.play("hurt.wav"); // Play Hurt Sound
                    delayEndStage.play();
                });
                delay.play();
            } else {
                FXGL.play("hurt.wav");
                enemyCC.setCurrentHp(enemyHp);
                enemyCC.setCurrentShield(enemyShield);
            }
        }
    }

    // Enemy Attack Function
    protected void enemyAttack(){
        int enemyDamage = enemyComps.get(enemyID).getDamage();
        // Check if enemy is a Boss or not
        if(isBoss){
            enemyDamage = enemyDamage * randomNumGenerator(1,2);
        }
        int damageAfterShield;
        // randomNum is to set the random target that the enemy will attack
        int randomNum = randomNumGenerator(0, 2);
        // If isGuard skill is true or activate, it will only attack the guard
        if(isGuard){
            randomNum = 0; // Set 0 since the guard is at 0
        }

        // Check Character Alive or Dead (To set the target for the enemy)
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
        damageAfterShield = enemyDamage - playerShield;
        // Check if enemy is dead or not. If dead, then the enemy cannot attack
        if(!isEnemyDead){
            if(damageAfterShield >= 0){
                CC.getShield().damage(playerShield);
                CC.getHp().damage(damageAfterShield);
                playerHP = playerHP - damageAfterShield;
                playerShield = 0;
            } else {
                playerShield = playerShield - enemyDamage;
                CC.getShield().damage(enemyDamage);
            }
        }

        // Check if enemy is dead or not
        if(!isEnemyDead){
            labelInfo.setText("Enemy deal " + enemyDamage + " damage to " + CC.getName());
        }

        // Check playerHP is 0 or below 0. If below 0, the character is dead
        if(playerHP <= 0){

            // Delay
            Timeline delay = new Timeline(
                    new KeyFrame(Duration.seconds(0.8)
                    ));
            int finalRandomNum = randomNum;
            delay.setOnFinished(e -> {
                labelInfo.setText("Your " + CC.getName() + " die");
                characterDead(CC, finalRandomNum); // Go to characterDead() function
                if(selectedBox == finalRandomNum){
                    selectHighLightBox(finalRandomNum); // Go to selectHighLightBox() function
                }
            });
            delay.play();
        } else {
            // Check if enemy is dead or not
            if(!isEnemyDead){
                FXGL.play("hurt.wav"); // Play Hurt Sound
                CC.setCurrentHp(playerHP);
                CC.setCurrentShield(playerShield);
            }
        }
    }

    // Set the character to dead by removing the player and panel, then set isP{number}Dead true
    protected void characterDead(CharacterComponent CC, int num){
        if(CC.getSkillName().equals("Guard Team")){
            isGuard = false;
        }
        FXGL.play("hurt.wav");
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

    // Set the selectedBox and actingCharacter to alive player
    protected void selectHighLightBox(int num){
        if (num == 0){
            if(!isP2Dead){
                selectedBox = 1;
                actingCharacter = characterComps.get(1);
            } else {
                selectedBox = 2;
                actingCharacter = characterComps.get(2);
            }
        } else if (num == 1) {
            if(!isP1Dead){
                selectedBox = 0;
                actingCharacter = characterComps.get(0);
            } else {
                selectedBox = 2;
                actingCharacter = characterComps.get(2);
            }
        } else if (num == 2) {
            if (!isP1Dead) {
                selectedBox = 0;
                actingCharacter = characterComps.get(0);
            } else {
                selectedBox = 1;
                actingCharacter = characterComps.get(1);
            }
        }
        highlight.setPosition(3, 100 + 180*selectedBox - 12);
    }

    // End Stage (End the stage by removing the box panels and enemy)
    protected void endStage(){
        stage = stage + 1;
        round = 0;

        // Remove Box Panels
        removeUINode(pane1);
        removeUINode(pane2);
        removeUINode(pane3);
        removeUINode(paneEnemy);

        // Remove Enemy
        enemy.removeFromWorld();

        // Go to the endStageScene() and nextStage() function
        endStageScene();
        nextStage();
    }

    // Set the End Stage Scene
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

    // Set the GameOver scene and function
    protected void GameOver(){
        // Update the HighScore into the database
        int tempScore;
        if(stage > highScore){
            tempScore = stage;
        } else {
            tempScore = highScore;
        }
        updateHighScore(tempScore);

        // Show Game Over
        labelInfo.setText("GAME OVER");
        gameOver.setVisible(true);

        // Delay
        Timeline delay = new Timeline(
                new KeyFrame(Duration.seconds(3)
                ));
        delay.setOnFinished(e -> {
            gameSound.stop(); // Stop Game Music Background
            getGameController().gotoMainMenu(); // Go To Main Menu (The login and registration page)
            });
        delay.play();
    }

    // Update High Score Function
    private boolean updateHighScore(int score) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "UPDATE users SET highscore = ? WHERE username = ?";
        List<String> params = new ArrayList<>();
        params.add(Integer.toString(score));
        params.add(userName);
        try {
            return jdbcUtils.insert(sql, params) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void main(String[] args) {
        launch(args);
    }
}



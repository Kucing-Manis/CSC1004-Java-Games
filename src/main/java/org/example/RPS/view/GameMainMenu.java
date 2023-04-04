package org.example.RPS.view;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.DialogService;
import com.almasb.fxgl.ui.FontType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.example.RPS.db.JdbcUtils;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;

public class GameMainMenu extends FXGLMenu {
    private final TranslateTransition tt1;
    private final TranslateTransition tt2;
    private final TranslateTransition tt3;
    private final Pane defaultPane;
    private boolean verify(String username, String password) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "SELECT count(1) count FROM `users` WHERE username = ? and password = ?";
        List<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        try {
            return jdbcUtils.count(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean exists(String username) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "SELECT count(1) count FROM `users` WHERE username = ?";
        List<String> params = new ArrayList<>();
        params.add(username);
        try {
            return jdbcUtils.count(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean signUp(String username, String password) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "insert into `users` (username,password) values (?,?)";
        List<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        try {
            return jdbcUtils.insert(sql, params) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public GameMainMenu() {
        super(MenuType.MAIN_MENU);
        Texture background = texture("menu/BackGroundScreen_Day.jpg");
        background.setLayoutX(-480);
        background.setLayoutY(-180);
        background.setScaleX(0.5);
        background.setScaleY(0.7);
        background.setVisible(true);

        Texture character = texture("menu/Character.png");
        character.setLayoutX(90);
        character.setLayoutY(380);
        character.setScaleX(1.8);
        character.setScaleY(1.8);
        character.setVisible(true);

        Texture bubbleBox = texture("menu/TextBubble.png");
        bubbleBox.setLayoutX(-75);
        bubbleBox.setLayoutY(-130);
        bubbleBox.setOpacity(0.85);
        bubbleBox.setScaleX(-0.27);
        bubbleBox.setScaleY(0.22);
        bubbleBox.setVisible(true);

        var titleText = FXGL.getUIFactoryService().newText("Rock Paper Scissor RPG", Color.BLACK, FontType.MONO, 48);
        titleText.setLayoutX(170);
        titleText.setLayoutY(140);

        MainMenuButton newGameBtn = new MainMenuButton("START GAME", this::fireNewGame);
        MainMenuButton helpBtn = new MainMenuButton("HELP", this::instructions);
        MainMenuButton exitBtn = new MainMenuButton("EXIT", () -> getGameController().exit());
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(newGameBtn, helpBtn, exitBtn);
        newGameBtn.setSelected(true);
        VBox menuBox = new VBox(
                5,
                newGameBtn,
                helpBtn,
                exitBtn
        );
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setLayoutX(505);
        menuBox.setLayoutY(330);
        menuBox.setVisible(false);


        Rectangle bgRect = new Rectangle(330, 220);
        bgRect.setLayoutX(520);
        bgRect.setLayoutY(320);
        bgRect.setOpacity(0.65);
        bgRect.setVisible(false);
        Line line = new Line(140, 160, 820, 160); //x,y x,y
        line.setStroke(Color.web("#000000"));
        line.setStrokeWidth(5);
//        Texture textureWall = texture("ui/fxgl.png");
//        textureWall.setLayoutX(310);
//        textureWall.setLayoutY(600);


        VBox loginBox = new VBox(6);//登录VBOX
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setLayoutX(550);
        loginBox.setLayoutY(360);
        loginBox.setVisible(false);

        VBox signUpBox = new VBox(6);//注册VBOX
        signUpBox.setAlignment(Pos.CENTER_LEFT);
        signUpBox.setLayoutX(550);
        signUpBox.setLayoutY(360);
        signUpBox.setVisible(false);

        TextField loginUsername = new TextField();//登录VBOX用户名
        loginUsername.setId("inputText");
        loginUsername.setPromptText("Username");

        TextField signUpUsername = new TextField();//注册VBOX用户名
        signUpUsername.setId("inputText");
        signUpUsername.setPromptText("Username");

        TextField loginPassword = new TextField();//登录VBOX密码
        loginPassword.setId("inputText");
        loginPassword.setPromptText("Password");

        TextField signUpPassword = new TextField();
        signUpPassword.setId("inputText");
        signUpPassword.setPromptText("Password");

        TextField signUpCheckPassword = new TextField();
        signUpCheckPassword.setId("inputText");
        signUpCheckPassword.setPromptText("Confirm Password");

        Label loginLabel = new Label();
        loginLabel.setId("label");

        Label registerLabel = new Label();
        registerLabel.setId("label");


        Button registerBtn = new Button();
        registerBtn.setId("loginBtn");
        registerBtn.setText("Register");
        registerBtn.setOnAction(event -> {
            if(!signUpPassword.getText().equals(signUpCheckPassword.getText())){
                registerLabel.setText("Password and Confirm Password are not same");
            } else {
                if (exists(signUpUsername.getText())) {
                    registerLabel.setText("Username already exists");
                } else {
                    if (signUp(signUpUsername.getText(), signUpPassword.getText())) {
                        signUpBox.setVisible(false);
                        loginBox.setVisible(true);
                    }
                }
            }
        });

        Button toSignUpBtn = new Button();
        toSignUpBtn.setId("loginBtn");
        toSignUpBtn.setText("Register");
        toSignUpBtn.setOnAction(event -> {
            loginBox.setVisible(false);
            signUpBox.setVisible(true);
        });


        Button loginBtn = new Button();//登录VBOX的 登录按钮
        loginBtn.setId("loginBtn");
        loginBtn.setText("Login");
        loginBtn.setOnAction(event -> {
            if (verify(loginUsername.getText(), loginPassword.getText())) {
                loginBox.setVisible(false);
                menuBox.setVisible(true);
            } else {
                loginLabel.setText("Incorrect username or password");
            }
        });

        //Add to Screen
        loginBox.getChildren().addAll(
                loginUsername,
                loginPassword,
                loginLabel,
                loginBtn,
                toSignUpBtn);
        signUpBox.getChildren().addAll(
                signUpUsername,
                signUpPassword,
                signUpCheckPassword,
                registerLabel,
                registerBtn);

//        double random = Math.random();
//        Texture imageRandom = FXGL.texture("ui/Scissor.png");
//        if(random <= 0.333333333){
//             imageRandom = FXGL.texture("ui/Rock.png");
//        } else if(random <= 0.666666666){
//             imageRandom = FXGL.texture("ui/Paper.jpeg");
//        } else {
//             imageRandom = FXGL.texture("ui/Scissor.png");
//        }
        Texture rock = FXGL.texture("ui/Rock.png");
        Texture paper = FXGL.texture("ui/Paper.jpeg");
        Texture scissor = FXGL.texture("ui/Scissor.png");
        rock.setLayoutX(-400);
        paper.setLayoutX(-400);
        scissor.setLayoutX(-400);
        rock.setOpacity(0.9);
        paper.setOpacity(0.9);
        scissor.setOpacity(0.9);

        rock.setScaleX(0.27);
        rock.setScaleY(0.27);
        paper.setScaleX(0.27);
        paper.setScaleY(0.27);
        scissor.setScaleX(0.27);
        scissor.setScaleY(0.27);
        tt1 = new TranslateTransition(Duration.seconds(1), scissor);
        tt1.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
        tt1.setFromX(-400);
        tt1.setFromY(447);
        tt1.setToX(940);
        tt1.setToY(447);
        tt2 = new TranslateTransition(Duration.seconds(1), paper);
        tt1.setOnFinished(e -> tt2.play());
        tt2.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
        tt2.setFromX(-400);
        tt2.setFromY(438);
        tt2.setToX(625);
        tt2.setToY(438);
        tt3 = new TranslateTransition(Duration.seconds(1), rock);
        tt2.setOnFinished(e -> tt3.play());
        tt3.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
        tt3.setFromX(-400);
        tt3.setFromY(450);
        tt3.setToX(340);
        tt3.setToY(450);
        tt3.setOnFinished(e -> {
            loginBox.setVisible(true);
            bgRect.setVisible(true);
        });

        defaultPane = new Pane(background, bgRect, bubbleBox, character, titleText, rock, paper, scissor, loginBox, menuBox, signUpBox, line);
        getContentRoot().getChildren().setAll(defaultPane);
    }


    @Override
    public void onCreate() {
        getContentRoot().getChildren().setAll(defaultPane);
//        FXGL.play("mainMenuLoad.wav");
        tt1.play();
    }

    /**
     * 显示玩家使用帮助.比如如何移动坦克,如何发射子弹
     */
    private void instructions() {
        GridPane pane = new GridPane();
        pane.setHgap(20);
        pane.setVgap(15);
        KeyView kvW = new KeyView(W);
        kvW.setPrefWidth(38);
        TilePane tp1 = new TilePane(kvW, new KeyView(S), new KeyView(A), new KeyView(D));
        tp1.setPrefWidth(200);
        tp1.setHgap(2);
        tp1.setAlignment(Pos.CENTER_LEFT);

        pane.addRow(0, getUIFactoryService().newText("Movement"), tp1);
        pane.addRow(1, getUIFactoryService().newText("Shoot"), new KeyView(F));
        KeyView kvL = new KeyView(LEFT);
        kvL.setPrefWidth(38);
        TilePane tp2 = new TilePane(new KeyView(UP), new KeyView(DOWN), kvL, new KeyView(RIGHT));
        tp2.setPrefWidth(200);
        tp2.setHgap(2);
        tp2.setAlignment(Pos.CENTER_LEFT);
        pane.addRow(2, getUIFactoryService().newText("Movement"), tp2);
        pane.addRow(3, getUIFactoryService().newText("Shoot"), new KeyView(SPACE));
        DialogService dialogService = getDialogService();
        dialogService.showBox("Help", pane, getUIFactoryService().newButton("OK"));
    }
}

//        Media media = new Media(getClass().getClassLoader().getResource("/assets/sounds/mainMenu.wav").toExternalForm());
//        MediaPlayer mainMenuSound = new MediaPlayer(media);
//        mainMenuSound.play();
//        mainMenuSound.setCycleCount(MediaPlayer.INDEFINITE);
//            mainMenuSound.stop();

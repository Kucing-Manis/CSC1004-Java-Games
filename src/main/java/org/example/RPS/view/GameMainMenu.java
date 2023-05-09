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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.example.RPS.RPSApp;
import org.example.RPS.db.JdbcUtils;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;

public class GameMainMenu extends FXGLMenu {
    private final TranslateTransition tt1;
    private final TranslateTransition tt2;
    private final TranslateTransition tt3;
    private final Pane defaultPane;
    private String userName;
    private int highScore;
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

    private boolean signUp(String username, String password, String email, String age, String gender) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        // Gender (0 = female, 1 = male)
        String sql = "insert into `users` (username, password, email, age, gender) values (?,?,?,?,?)";
        List<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        params.add(email);
        params.add(age);
        params.add(gender);
        try {
            return jdbcUtils.insert(sql, params) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getHighScore(String username) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "SELECT highscore FROM `users` WHERE username = ?";
        List<String> params = new ArrayList<>();
        params.add(username);
        try {
            return jdbcUtils.getHighScorefromSQL(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 9;
    }

    public GameMainMenu() {
        super(MenuType.MAIN_MENU);

        Media media = new Media(getClass().getResource("/assets/sounds/mainMenuMusic.wav").toExternalForm());
        MediaPlayer mainMenuSound = new MediaPlayer(media);
        mainMenuSound.play();
        mainMenuSound.setCycleCount(MediaPlayer.INDEFINITE);
        mainMenuSound.setVolume(0.3);

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
        titleText.setLayoutY(140);                                                                                                var bubbleText1 = FXGL.getUIFactoryService().newText("Welcome!", Color.BLACK, FontType.MONO, 30);
                                                                                                                                       bubbleText1.setLayoutX(310);
        bubbleText1.setLayoutY(245);

        var bubbleText2 = FXGL.getUIFactoryService().newText("", Color.BLACK, FontType.MONO, 20);
        bubbleText2.setLayoutX(298);
        bubbleText2.setLayoutY(245);
        bubbleText2.setVisible(false);

        MainMenuButton newGameBtn = new MainMenuButton("START GAME", () -> {
           mainMenuSound.stop();
            RPSApp.setData(userName, highScore);
           fireNewGame();
        });
        MainMenuButton exitBtn = new MainMenuButton("EXIT", () -> getGameController().exit());
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(newGameBtn, exitBtn);
        newGameBtn.setSelected(true);
        VBox menuBox = new VBox(
                2,
                newGameBtn,
                exitBtn
        );
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setLayoutX(505);
        menuBox.setLayoutY(400);
        menuBox.setVisible(false);


        Rectangle bgRect = new Rectangle(330, 220);
        bgRect.setLayoutX(520);
        bgRect.setLayoutY(320);
        bgRect.setOpacity(0.65);
        bgRect.setVisible(false);
        Rectangle bgRect2 = new Rectangle(330, 100);
        bgRect2.setLayoutX(520);
        bgRect2.setLayoutY(220);
        bgRect2.setOpacity(0.65);
        bgRect2.setVisible(false);
        Line line = new Line(140, 160, 820, 160); //x,y x,y
        line.setStroke(Color.web("#000000"));
        line.setStrokeWidth(5);

        VBox loginBox = new VBox(6);
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setLayoutX(550);
        loginBox.setLayoutY(360);
        loginBox.setVisible(false);

        VBox signUpBox = new VBox(9);
        signUpBox.setAlignment(Pos.CENTER_LEFT);
        signUpBox.setLayoutX(550);
        signUpBox.setLayoutY(260);
        signUpBox.setVisible(false);

        TextField loginUsername = new TextField();
        loginUsername.setId("inputText");
        loginUsername.setPromptText("Username");

        TextField loginPassword = new TextField();
        loginPassword.setId("inputText");
        loginPassword.setPromptText("Password");

        TextField signUpUsername = new TextField();
        signUpUsername.setId("inputText");
        signUpUsername.setPromptText("Username");

        TextField signUpPassword = new TextField();
        signUpPassword.setId("inputText");
        signUpPassword.setPromptText("Password");

        TextField signUpCheckPassword = new TextField();
        signUpCheckPassword.setId("inputText");
        signUpCheckPassword.setPromptText("Confirm Password");

        TextField signUpEmail = new TextField();
        signUpEmail.setId("inputText");
        signUpEmail.setPromptText("Email");

        TextField signUpAge = new TextField();
        signUpAge.setId("inputText");
        signUpAge.setPromptText("Age");

        TextField signUpGender = new TextField();
        signUpGender.setId("inputText");
        signUpGender.setPromptText("Male or Female");


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
                    if (signUp(signUpUsername.getText(), signUpPassword.getText(), signUpEmail.getText(), signUpAge.getText(), signUpGender.getText())) {
                        signUpBox.setVisible(false);
                        bgRect2.setVisible(false);
                        loginBox.setVisible(true);
                        loginLabel.setText("Register Success");
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
            bgRect2.setVisible(true);
        });


        Button loginBtn = new Button();
        loginBtn.setId("loginBtn");
        loginBtn.setText("Login");
        loginBtn.setOnAction(event -> {
            if (verify(loginUsername.getText(), loginPassword.getText())) {
                System.out.println("Test1 "+ verify(loginUsername.getText(), loginPassword.getText()));
                System.out.println(loginUsername.getText() + "  " + loginPassword.getText());
                loginBox.setVisible(false);
                bubbleText1.setVisible(false);
                bubbleText2.setVisible(true);
                userName = loginUsername.getText();
                menuBox.setVisible(true);
                highScore = getHighScore(userName);
                bubbleText2.setText("High Score: " + highScore);
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
                signUpEmail,
                signUpAge,
                signUpGender,
                signUpPassword,
                signUpCheckPassword,
                registerLabel,
                registerBtn);

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

        defaultPane = new Pane(background, bgRect, bgRect2, bubbleBox, bubbleText2, bubbleText1, character, titleText, rock, paper, scissor, loginBox, menuBox, signUpBox, line);
        getContentRoot().getChildren().setAll(defaultPane);
    }


    @Override
    public void onCreate() {
        getContentRoot().getChildren().setAll(defaultPane);
        FXGL.play("mainMenuLoad.wav");
        tt1.play();
    }
}

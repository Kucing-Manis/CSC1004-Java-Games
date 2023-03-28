package org.example.RPS.view;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.DialogService;
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
    private final TranslateTransition tt;
    private final Pane defaultPane;
    private boolean verify(String username, String password) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "select count(1) count from `user` where username = ? and password = ?";
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

        String sql = "select count(1) count from `user` where username = ?";
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

        String sql = "insert into `user` (username,password) values (?,?)";
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
        Texture texture = texture("ui/logo.png");
        texture.setLayoutX(144);
        texture.setLayoutY(160);


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
        menuBox.setLayoutX(240);
        menuBox.setLayoutY(360);
        menuBox.setVisible(false);


        Rectangle bgRect = new Rectangle(getAppWidth(), getAppHeight());
        Line line = new Line(30, 580, 770, 580);
        line.setStroke(Color.web("#B9340D"));
        line.setStrokeWidth(2);
        Texture textureWall = texture("ui/fxgl.png");
        textureWall.setLayoutX(310);
        textureWall.setLayoutY(600);


        VBox loginBox = new VBox(6);//登录VBOX
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setLayoutX(280);
        loginBox.setLayoutY(360);
        loginBox.setVisible(false);

        VBox signUpBox = new VBox(5);//注册VBOX
        signUpBox.setAlignment(Pos.CENTER_LEFT);
        signUpBox.setLayoutX(280);
        signUpBox.setLayoutY(360);
        signUpBox.setVisible(false);

        TextField loginBoxUsername = new TextField();//登录VBOX用户名
        loginBoxUsername.setId("inputText");
        loginBoxUsername.setPromptText("username");

        TextField signUpUsername = new TextField();//注册VBOX用户名
        signUpUsername.setId("inputText");
        signUpUsername.setPromptText("username");

        TextField loginBoxPassword = new TextField();//登录VBOX密码
        loginBoxPassword.setId("inputText");
        loginBoxPassword.setPromptText("password");

        TextField signUpPassword = new TextField();//注册VBOX密码
        signUpPassword.setId("inputText");
        signUpPassword.setPromptText("password");

        Label loginBoxLabel = new Label();//登录VBOX错误提示label
        loginBoxLabel.setId("label");

        Label signUpBoxLabel = new Label();//注册VBOX错误提示label
        signUpBoxLabel.setId("label");


        Button signUpBoxBtn = new Button();//注册VBOX的 注册按钮
        signUpBoxBtn.setId("loginBtn");
        signUpBoxBtn.setText("Sign up");
        signUpBoxBtn.setOnAction(event -> {
            if (exists(signUpUsername.getText())) {
                signUpBoxLabel.setText("username already exists.");
            } else {
                if (signUp(signUpUsername.getText(), signUpPassword.getText())) {
                    signUpBox.setVisible(false);
                    menuBox.setVisible(true);
                }

            }
        });

        Button signUpBtn = new Button();//登录VBOX的 注册按钮
        signUpBtn.setId("loginBtn");
        signUpBtn.setText("Sign up");
        signUpBtn.setOnAction(event -> {
            loginBox.setVisible(false);
            signUpBox.setVisible(true);
        });


        Button loginBtn = new Button();//登录VBOX的 登录按钮
        loginBtn.setId("loginBtn");
        loginBtn.setText("Sign in");
        loginBtn.setOnAction(event -> {
            if (verify(loginBoxUsername.getText(), loginBoxPassword.getText())) {
                loginBox.setVisible(false);
                menuBox.setVisible(true);
            } else {
                loginBoxLabel.setText("Incorrect username or password.");
            }
        });

        //登录VBOX的功能
        loginBox.getChildren().addAll(
                loginBoxUsername,
                loginBoxPassword,
                loginBoxLabel,
                loginBtn,
                signUpBtn);
        //注册VBOX的功能
        signUpBox.getChildren().addAll(
                signUpUsername,
                signUpPassword,
                signUpBoxLabel,
                signUpBoxBtn);

        Texture tankTexture = FXGL.texture("ui/tankLoading.png");

        tt = new TranslateTransition(Duration.seconds(1), tankTexture);
        tt.setInterpolator(Interpolators.ELASTIC.EASE_OUT());
        tt.setFromX(172);
        tt.setFromY(252);
        tt.setToX(374);
        tt.setToY(252);
        tt.setOnFinished(e -> loginBox.setVisible(true));

        defaultPane = new Pane(bgRect, texture, tankTexture, loginBox, menuBox, signUpBox, line, textureWall);
        getContentRoot().getChildren().setAll(defaultPane);
    }


    @Override
    public void onCreate() {
        getContentRoot().getChildren().setAll(defaultPane);
        FXGL.play("mainMenuLoad.wav");
        tt.play();
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

package org.example.RPS.view;

import com.almasb.fxgl.texture.Texture;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.texture;

public class MainMenuButton extends RadioButton {

    public MainMenuButton(String text, Runnable action) {
        Texture texture = texture("ui/Point.png");
        texture.setVisible(false);
        texture.setRotate(90);
        setGraphic(texture);
        setGraphicTextGap(30);
        getStyleClass().add("main-menu-btn");
        setText(text);
        // Set Sound Effect
        selectedProperty().addListener((ob, ov, nv) -> texture.setVisible(nv));
        setOnMouseClicked(event -> {
            play("select.wav");
            action.run();
        });
        setOnMouseEntered(e -> {
                    play("mainMenuHover.wav");
                    setSelected(true);
                }
        );
        focusedProperty().addListener((ob, ov, nv) -> {
            if (nv) {
                play("mainMenuHover.wav");
                setSelected(true);
            }
        });
    }
}

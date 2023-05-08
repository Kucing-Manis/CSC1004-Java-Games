package org.example.RPS.component;

import com.almasb.fxgl.dsl.components.view.ChildViewComponent;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

public class boxViewComponent extends ChildViewComponent {
    private boxComponent box;
    private int BOX_WIDTH = 240;
    private int BOX_HEIGHT = 320;
    private int BOX_IMAGE_WIDTH = 200;
    private int BOX_IMAGE_HEIGHT = 300;

    @Override
    public void onAdded() {
        super.onAdded();

        Rectangle border = new Rectangle(BOX_WIDTH, BOX_HEIGHT);
        border.setStrokeWidth(2);
        border.setArcWidth(10);
        border.setArcHeight(10);

        Shape borderShape = Shape.union(border, new Circle(BOX_WIDTH / 2.0, 0.0, 30));
        borderShape.setFill(Color.DARKGRAY.darker().darker());
        borderShape.setStroke(Color.BLACK);

        var innerBorder = new Rectangle(BOX_WIDTH - 12, BOX_HEIGHT - 12);
        innerBorder.setFill(Color.color(0.75, 0.75, 0.75, 0.8));
        innerBorder.setStroke(Color.BLACK);
        innerBorder.setStrokeWidth(2);
        innerBorder.setArcWidth(15);
        innerBorder.setArcHeight(15);
        innerBorder.setTranslateX(6);
        innerBorder.setTranslateY(6);



        var imageRect = texture("characters/" + box.getData().getName() + ".png");
        imageRect.setTranslateX((BOX_WIDTH - BOX_IMAGE_WIDTH) / 2.0);
        imageRect.setTranslateY(45);

        var imageBorderRect = new Rectangle(BOX_IMAGE_WIDTH, BOX_IMAGE_HEIGHT, null);
        imageBorderRect.setStroke(Color.BLACK);
        imageBorderRect.setTranslateX(imageRect.getTranslateX());
        imageBorderRect.setTranslateY(imageRect.getTranslateY());


        var title = new Title(box.getName(), box.getLevel(), box.getData().getSkillName(), Color.DARKGRAY.darker().darker() );
        title.setTranslateX(imageRect.getTranslateX());
        title.setTranslateY(7.5);

        var iconAtk = texture("icon_atk.png", 512 / 16.0, 512 / 16.0).brighter().saturate();

        Text textAtk = getUIFactoryService().newText("", Color.BLACK, 18);
        textAtk.textProperty().bind(box.attackProperty().asString("%d"));

        var boxAtk = new HBox(0, iconAtk, textAtk);
        boxAtk.setAlignment(Pos.CENTER_LEFT);
        boxAtk.setTranslateX(17.5);
        boxAtk.setTranslateY(BOX_HEIGHT - 70);

        ProgressBar barHP = new ProgressBar(false);
        barHP.setMaxValue(box.getHp().getMaxValue());
        barHP.currentValueProperty().bind(box.getHp().valueProperty());
        barHP.setFill(Color.LIGHTGREEN);
        barHP.setWidth(BOX_WIDTH / 3.2);
        barHP.setHeight(12.5);

        ProgressBar barShield = new ProgressBar(false);
        barHP.setMaxValue(box.getShield().getMaxValue());
        barHP.currentValueProperty().bind(box.getShield().valueProperty());
        barHP.setFill(Color.LIGHTGRAY);
        barHP.setWidth(BOX_WIDTH / 3.2);
        barHP.setHeight(12.5);

        Text textHP = getUIFactoryService().newText("", Color.BLACK, 18);
        textHP.textProperty().bind(box.getHp().valueProperty().asString("%d"));

        Text textShield = getUIFactoryService().newText("", Color.BLACK, 18);
        textHP.textProperty().bind(box.getHp().valueProperty().asString("%d"));

        var boxHP = new HBox(-10, barHP, textHP);
        boxHP.setAlignment(Pos.CENTER_LEFT);
        boxHP.setTranslateX(BOX_WIDTH / 3.0);
        boxHP.setTranslateY(BOX_HEIGHT - 65);

        var boxShield = new HBox(-10, barShield, textShield);
        boxShield.setAlignment(Pos.CENTER_LEFT);
        boxShield.setTranslateX(BOX_WIDTH / 3.0);
        boxShield.setTranslateY(BOX_HEIGHT - 40);

        getViewRoot().getChildren().addAll(borderShape, innerBorder, imageRect, imageBorderRect , title, boxAtk , boxHP, boxShield);



        getViewRoot().setEffect(new DropShadow(10, -3.5, 10, Color.BLACK));

        getViewRoot().opacityProperty().bind(
                Bindings.when(box.aliveProperty()).then(1.0).otherwise(0.25)
        );

        // to improve animation performance when animating anything related to Text
        getViewRoot().setCache(true);
        getViewRoot().setCacheHint(CacheHint.SPEED);
    }

    private static class Title extends HBox {

        Title(String name, int level, String skillName, Color outlineColor) {
            super(-15);

            Circle circle = new Circle(20, 20, 20, Color.WHITE);
            circle.setStrokeWidth(2.0);
            circle.setStroke(outlineColor);

            var stack = new StackPane(circle, getUIFactoryService().newText("" + level, Color.BLACK, 30.0));

            Rectangle rect = new Rectangle(180, 30, Color.color(1, 1, 1, 0.8));
            rect.setStroke(Color.BLACK);

            getChildren().addAll(stack, new StackPane(rect, getUIFactoryService().newText(name, Color.BLACK, 16.0)));
            getChildren().addAll(stack, new StackPane(rect, getUIFactoryService().newText(skillName, Color.BLACK, 16.0)));

            stack.toFront();
        }
    }
}

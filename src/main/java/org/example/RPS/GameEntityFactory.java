package org.example.RPS;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.example.RPS.component.CharacterComponent;
import org.example.RPS.component.boxComponent;
import org.example.RPS.component.boxViewComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;
import static org.example.RPS.GameType.*;

public class GameEntityFactory implements EntityFactory {

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .view("Forest_Morning.png")
                .scale(0.95,1 )
                .build();
    }

//    @Spawns("boxPanel")
//    public Entity newBoxPanel(SpawnData data){
//        CharacterComponent CC = data.get("boxPanel");
//
//        return entityBuilder(data)
//                .with(new HealthIntComponent(CC.getHp()))
//                .with(new HealthIntComponent(CC.getShield()))
//                .with(new boxComponent(CC))
//                .with(new boxViewComponent())
//                .build();
//    }

//    @Spawns("boxPlaceHolder")
//    public Entity newPlaceholder(SpawnData data) {
//        var bg = new Region();
//        bg.setPrefSize(240, 320);
//        bg.setStyle(
//                "-fx-background-color: gold;" +
//                "-fx-border-color: black;" +
//                "-fx-border-width: 5;" +
//                "-fx-border-style: line-cap round;"
//        );
//        var text = getUIFactoryService().newText(data.get("isPlayer") ? "+" : "?", Color.WHITE, 76);
//        text.setStroke(Color.BLACK);
//        text.setStrokeWidth(2);
//
//        return entityBuilder(data)
//                .view(new StackPane(bg, text))
//                .build();
//    }
}

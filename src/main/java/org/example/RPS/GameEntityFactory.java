package org.example.RPS;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class GameEntityFactory implements EntityFactory {

    // Spawn Game Background Image
    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .view("Forest_Morning.png")
                .scale(0.95,1 )
                .build();
    }
}

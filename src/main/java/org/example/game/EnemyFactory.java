package org.example.game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnemyFactory implements EntityFactory {

    @Spawns("enemy")
    public Entity newEnemy(SpawnData enemyData) {
        return FXGL.entityBuilder(enemyData)
                .view(new Rectangle(40, 40, Color.RED))
                .buildAndAttach();
    }
}

package org.example.RPS;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.RPS.component.CharacterComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static org.example.RPS.GameType.*;

public class GameEntitiyFactory implements EntityFactory {

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .view("Forest_Morning.png")
                .scale(0.95,1 )
                .build();
    }

    @Spawns("hero")
    public Entity newHero(SpawnData data){
        CharacterComponent heroComp = new CharacterComponent("Hero", "Justice", 1,1,1,1,1 );
//        heroComp.setLevel(stage);  // Level = Stage Level
        heroComp.setDamage((int)heroComp.getLevel()/5 + heroComp.getDamage());
        heroComp.setHp((int)heroComp.getLevel()/3 + heroComp.getHp());
        heroComp.setShield((int)heroComp.getLevel()/10 + heroComp.getShield());

        return entityBuilder(data)
                .type(PLAYER)
                .build();
    }

}

package org.example.RPS.animation;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;


public class AnimationComponent extends Component {
    private int once = 0;
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private Boolean isPlayer;

    public AnimationComponent(SpriteData spriteData, Boolean isPLAYER) {
        // Set the idle animation (Character Punching)
        animIdle = new AnimationChannel(FXGL.image(spriteData.getName()), spriteData.getRow(), spriteData.getWidth(), spriteData.getHeight(), Duration.seconds(spriteData.getTimeSec()), spriteData.getStart(), spriteData.getEnd());
        texture = new AnimatedTexture(animIdle);
        isPlayer = isPLAYER;
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        // Check if it is player or not
        if(isPlayer) {
            // If player, mirror the image from left to right
            entity.getTransformComponent().setScaleX(-1);
        } else {
            // If enemy, make the scale or size bigger
            entity.getTransformComponent().setScaleX(2);
            entity.getTransformComponent().setScaleY(2);
        }
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
            if(once == 0) // Set once == 0 so texture.loopAnimationChannel only call one time
            texture.loopAnimationChannel(animIdle);
            once = 1;
        }
}
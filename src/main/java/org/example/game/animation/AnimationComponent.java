package org.example.game.animation;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;


public class AnimationComponent extends Component {
    private int once = 0;
    private int speed;
    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public AnimationComponent(SpriteData spriteData) {
        animIdle = new AnimationChannel(FXGL.image(spriteData.getName()), spriteData.getRow(), spriteData.getWidth(), spriteData.getHeight(), Duration.seconds(spriteData.getTimeSec()), spriteData.getStart(), spriteData.getEnd());
        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getTransformComponent().setScaleX(-1);
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateX(speed * tpf);
            if(once == 0)
            texture.loopAnimationChannel(animIdle);
            once = 1;
        }
    public void moveRight() {
        speed = 150;

        getEntity().setScaleX(-1);
    }

    public void moveLeft() {
        speed = -150;

        getEntity().setScaleX(1);
    }
}
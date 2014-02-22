package platformgame;

import org.newdawn.slick.Animation;

public class Sprite {

    public Animation animation;
    public HitBox hitBox;

    public Sprite(Animation animation, HitBox hitBox) {
        this.animation = animation;
        this.hitBox = hitBox;
    }

    public void render(int x, int y) {
        animation.draw(x - hitBox.getXOffset(), y - hitBox.getYOffset());
    }

    public void render(int x, int y, int width, int height) {
        animation.draw(x - hitBox.getXOffset(), y - hitBox.getYOffset(), width, height);
    }
}

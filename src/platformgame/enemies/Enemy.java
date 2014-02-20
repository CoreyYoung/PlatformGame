package platformgame.enemies;

import org.newdawn.slick.Animation;
import platformgame.MovingEntity;
import platformgame.StatePlaying;

public abstract class Enemy extends MovingEntity {

    public int healthMAX;
    public int speedMAX;
    public float ACCELERATION;

    public Animation sprite;

    public int defense;
    public int stability;
    public int damage;
    public int knockback;

    public boolean awake;
    public int health;

    @Override
    public void update() {
        super.update();
    }

    public void render(int camX, int camY) {
        if (x < StatePlaying.player.x) {
            sprite.draw((int) (camX + x), (int) (camY + y));
        } else {
            sprite.draw((int) (camX + x + width), (int) (camY + y), -width, height);
        }
    }

    @Override
    public void jump() {
        super.jump();
    }
}

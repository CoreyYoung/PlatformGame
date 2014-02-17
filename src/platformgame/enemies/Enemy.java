package platformgame.enemies;

import org.newdawn.slick.Image;
import platformgame.MovingEntity;

public abstract class Enemy extends MovingEntity {

    public int healthMAX;
    public int speedMAX;
    public float ACCELERATION;

    public Image sprite;

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

    }

    @Override
    public void jump() {
        super.jump();
    }
}

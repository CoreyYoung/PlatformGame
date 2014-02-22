package platformgame.enemies;

import platformgame.MovingEntity;
import platformgame.Sprite;
import platformgame.StatePlaying;

public abstract class Enemy extends MovingEntity {

    public int healthMAX;
    public int speedMAX;
    public float ACCELERATION;

    public Sprite sprite;

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
            sprite.render((int) (camX + x), (int) (camY + y));
        } else {
            sprite.render((int) (camX + x + width), (int) (camY + y), -sprite.animation.getWidth(), sprite.animation.getHeight());
        }
    }

    @Override
    public void jump() {
        super.jump();
    }
}

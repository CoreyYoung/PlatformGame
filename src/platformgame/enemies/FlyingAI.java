package platformgame.enemies;

import org.newdawn.slick.Image;
import platformgame.StatePlaying;
import platformgame.Vector;

public class FlyingAI extends Enemy {

    private final int MAX_SPEED;

    public FlyingAI(Image sprite, int x, int y, boolean awake, int healthMAX, int MAX_SPEED,
            float ACCELERATION, int defense, int stability, int damage, int knockback) {
        this.sprite = sprite;
        this.x = x;
        this.awake = awake;
        this.healthMAX = healthMAX;
        this.MAX_SPEED = MAX_SPEED;
        this.ACCELERATION = ACCELERATION;
        this.defense = defense;
        this.stability = stability;
        this.damage = damage;
        this.knockback = knockback;

        health = healthMAX;
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    @Override
    public void update() {
        float dir = Vector.getDir(StatePlaying.player.x, StatePlaying.player.y, x, y);
        Vector acceleration = new Vector(dir, ACCELERATION);

        velocity.addVector(acceleration);

        velocity.setMagnitude(Math.min(velocity.getMagnitude(), MAX_SPEED));

        x += velocity.getXMagnitude();
        y += velocity.getYMagnitude();
    }

    @Override
    public void render(int camX, int camY) {
        sprite.draw(camX + x, camY + y);
    }
}

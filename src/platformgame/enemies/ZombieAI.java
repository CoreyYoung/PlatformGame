package platformgame.enemies;

import org.newdawn.slick.Animation;
import platformgame.StatePlaying;
import platformgame.World;

public class ZombieAI extends Enemy {

    public ZombieAI(Animation sprite, int x, int y, boolean awake, int healthMAX, int speedMAX,
            int JUMP_SPEED, float ACCELERATION, int defense, int stability, int damage, int knockback) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.awake = awake;
        this.healthMAX = healthMAX;
        this.speedMAX = speedMAX;
        this.JUMP_SPEED = JUMP_SPEED;
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
    public void movement() {
        if (StatePlaying.player.x < x) {
            velocity.setXMagnitude(Math.max(velocity.getXMagnitude() - ACCELERATION, -speedMAX));
        } else {
            velocity.setXMagnitude(Math.min(velocity.getXMagnitude() + ACCELERATION, speedMAX));
        }

        if (StatePlaying.player.y + 64 + 48 < y + height) {
            super.jump();
        }

        if (!World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + height) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + width - 1) / 32), (int) Math.floor((y + height) / 32))) {
            float yspeed = Math.min(velocity.getYMagnitude() + World.GRAVITY, World.yspeedMAX);
            velocity.setYMagnitude(yspeed);
        }

        int tempX;
        if (velocity.getXMagnitude() < 0) {
            tempX = (int) (x + velocity.getXMagnitude());
        } else {
            tempX = (int) (x + velocity.getXMagnitude() + width);
        }

        Enemy enemy = enemyAtPosition((int) (tempX), (int) (y + velocity.getYMagnitude()));
        if (enemy != null) {
            if (Math.signum(enemy.x - x) == Math.signum(velocity.getXMagnitude())) {
                velocity.setXMagnitude(0);
            }
        }
    }

    public Enemy enemyAtPosition(int x, int y) {
        for (Enemy enemy : EnemyHandler.enemyList) {
            if (enemy != this && enemy != null) {
                if (enemy.x <= x + width && enemy.x + width >= x
                        && enemy.y <= y + height && enemy.y + 32 >= y) {
                    return enemy;
                }
            }
        }

        return null;
    }
}

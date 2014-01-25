package platformgame.Enemies;

import org.newdawn.slick.Image;
import platformgame.Player;
import platformgame.World;

public class ZombieAI extends Enemy {
    private final int JUMP_SPEED;
    private final int width, height;
    
        public ZombieAI(Image sprite, int x, int y, boolean awake, int healthMAX, int speedMAX,
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

    public void render(int camX, int camY) {
        if (x <= Player.x) {
            sprite.draw(x + camX, y + camY);
        } else {
            sprite.draw((int) x + camX, (int) y + camY, width - 1, 0, 0, height);
        }
    }
    
    public void update() {
        movement();
        collisions();
    }
    
    public void movement() {
        if (Player.x < x) {
            xspeed = Math.max(xspeed - ACCELERATION, -speedMAX);
        } else if (Player.x > x) {
            xspeed = Math.min(xspeed + ACCELERATION, speedMAX);
        } else {
            xspeed = 0;
        }

        if (Player.y + 64 + 48 < y + height) {
            jump();
        }

        if (!World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + height) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + width - 1) / 32), (int) Math.floor((y + height) / 32))) {
            yspeed = Math.min(yspeed + World.GRAVITY, World.yspeedMAX);
        }
        
        int tempX;
        if (xspeed < 0) {
            tempX = (int) (x+xspeed);
        } else {
            tempX = (int) (x+xspeed+width);
        }
        
        ZombieAI zombie = zombieAtPosition((int) (tempX), (int) (y + yspeed));
        if (zombie != null) {
            if (Math.signum(zombie.x - x) == Math.signum(xspeed)) {
                xspeed = 0;
            }
        }
    }
    
    public void collisions() {

        if (onSlope(World.LEFT_SLOPE)) {
            performLeftSlopeCollision();
        } else if (onSlope(World.RIGHT_SLOPE)) {
            performRightSlopeCollision();
        } else {
            performBlockCollisions();
        }
    }

    private boolean onSlope(int tileTypeNum) {
        final int x1 = (int) x + width - 1;
        final int x2 = (int) x + 1;
        
        final int tempY = (int) (y + height - 1);

        return (World.getTileAtPoint((int) x1 / 32, tempY / 32) == tileTypeNum
                || World.getTileAtPoint((int) x2 / 32, tempY / 32) == tileTypeNum);
    }

    private void performLeftSlopeCollision() {
        int gridPadding = (int) (Math.ceil((double) height / 32) * 32) - height;
        int tempX = (int) x + 1;
        int roundedY = (int) (Math.floor((y - 1 + 32 - gridPadding) / 32) * 32 );
        
        int moveY = roundedY + (gridPadding - 32) + (int) (tempX - Math.floor(tempX / 32) * 32);
        
        performSlopeCollisions(World.LEFT_SLOPE, -1, tempX, moveY);
    }

    private void performRightSlopeCollision() {
        int gridPadding = (int) (Math.ceil((double) height / 32) * 32) - height;
        int tempX = (int) x + width - 1;
        int roundedY = (int) (Math.floor((y - 1 + 32 - gridPadding) / 32) * 32 );
        
        int moveY = roundedY + (gridPadding - 32) + (int) (32 - (tempX - Math.floor(tempX / 32) * 32));
        
        performSlopeCollisions(World.RIGHT_SLOPE, 1, tempX, moveY);
    }

    private void performSlopeCollisions(int tileTypeNum, int dir, int tempX, int moveY) {
        int gridPadding = (int) (Math.ceil((double) height / 32) * 32) - height;
        int roundedY = (int) (Math.floor((y - 1) / 32) * 32 );
        int slopeTop = roundedY + gridPadding;
        
        x += xspeed;

        if (World.getTileAtPoint((int) (tempX + xspeed) / 32, (int) (y + height - 1) / 32) == tileTypeNum
                || Math.signum(xspeed) != dir) {
            if (y > moveY) {
                y = moveY;
            }
        } else {
            y = slopeTop;
        }

        if (y + yspeed <= moveY) {
            y += yspeed;
        } else {
            yspeed = 0;
            y = moveY;
        }

        if (World.isBlockAtPoint((int) (x + 32) / 32, (int) y / 32)) {
            x = (int) (x / 32) * 32;
        } else if (World.isBlockAtPoint((int) (x) / 32, (int) y / 32)) {
            x = (int) ((x + 32) / 32) * 32;
        }
    }

    private void performBlockCollisions() {
        if (xspeed != 0) {
            int sign = (int) Math.signum(xspeed);
            int tempX, pos, destX;
            
            if (sign == -1) {
                tempX = (int) Math.floor((x + xspeed) / 32);
                pos = (int) Math.floor(x / 32);
                destX = (int) Math.floor(x / 32) * 32;
            } else {
                tempX = (int) Math.floor((x + xspeed + width) / 32);
                pos = (int) (Math.ceil(x / 32) - Math.floor((32 - width) / 32) + 1);
                destX = (int) Math.floor(x / 32) * 32 + 32 - width;
            }
            
            if (! World.isBlockInLine(tempX, tempX, (int) Math.floor(y / 32), (int) Math.floor((y + height - 1) / 32))) {
                x += xspeed;
            } else {
                jump();
                xspeed = 0;
                if (! World.isBlockInLine(pos, pos, (int) Math.floor(y / 32), (int) Math.floor((y + height - 1) / 32))) {
                    x = destX;
                } else {
                    x = Math.round(x);
                }
            }
        }
        
        int pos = 0;
        int gridPadding = (int) (Math.ceil((double) height / 32) * 32) - height;
        if (yspeed < 0) {
            pos = (int) Math.floor(y / 32) * 32 + 1;
        } else if (yspeed > 0) {
            pos = (int) Math.floor((y + 32 - (gridPadding + 1)) / 32) * 32;
        }
        
        if (!World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + height + yspeed) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + width - 1) / 32), (int) Math.floor((y + height + yspeed) / 32))
                && !World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + yspeed) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + width - 1) / 32), (int) Math.floor((y + yspeed) / 32))) {
            y += yspeed;
        } else {
            if (yspeed != 0) {
                if (yspeed < 0) {
                    System.out.println("Y: " + y);
                }
                y = pos;
            }
            yspeed = 0;
        }
    }
    
    public void jump() {
        if (World.isBlockAtPoint((int) (x / 32), (int) ((y + height) / 32))
                || World.isBlockAtPoint((int) ((x + width - 1) / 32), (int) ((y + height) / 32))) {
            yspeed = JUMP_SPEED;
        }
    }
    
    public ZombieAI zombieAtPosition(int x, int y) {
        for (ZombieAI zombie : EnemyHandler.zombieList) {
            if (zombie != this && zombie != null) {
                if (zombie.x <= x + width && zombie.x + width >= x
                        && zombie.y <= y + height && zombie.y + 32 >= y) {
                    return zombie;
                }
            }
        }
        
        return null;
    }
}
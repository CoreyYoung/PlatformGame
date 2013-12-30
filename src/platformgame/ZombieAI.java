package platformgame;

import org.newdawn.slick.Image;

public class ZombieAI {
    public int healthMAX = 10;
    public int speedMAX = 3;
    public int JUMP_SPEED = -8;
    public float ACCELERATION = 0.1f;

    //public int id;
    public Image sprite;

    public int defense = 2;
    public int stability = 0;
    public int damage = 5;
    public int knockback = 3;

    public float x;
    public float y;
    public float xspeed = 0;
    public float yspeed = 0;
    
    public boolean awake = false;
    public int health = healthMAX;
    
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
    }

    public void render(int camX, int camY) {
        if (x <= Player.x) {
            sprite.draw(x + camX, y + camY);
        } else {
            sprite.draw((int) x + camX, (int) y + camY, 31, 0, 0, 64);
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

        if (Player.y + 48 < y) {
            jump();
        }

        if (!World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + 64) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + 31) / 32), (int) Math.floor((y + 64) / 32))) {
            yspeed = Math.min(yspeed + World.GRAVITY, World.yspeedMAX);
        }
        
        int tempX;
        if (xspeed < 0) {
            tempX = (int) (x+xspeed);
        } else {
            tempX = (int) (x+xspeed+32);
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
        final int x1 = (int) x + 31;
        final int x2 = (int) x + 1;

        return (World.getTileAtPoint((int) x1 / 32, (int) (y + 63) / 32) == tileTypeNum
                || World.getTileAtPoint((int) x2 / 32, (int) (y + 63) / 32) == tileTypeNum);
    }

    private void performLeftSlopeCollision() {
        int tempX = (int) x + 1;
        int moveY = (int) Math.floor((y - 1) / 32) * 32 + (int) (tempX - Math.floor(tempX / 32) * 32);
        performSlopeCollisions(World.LEFT_SLOPE, -1, tempX, moveY);
    }

    private void performRightSlopeCollision() {
        int tempX = (int) x + 31;
        int moveY = (int) Math.floor((y - 1) / 32) * 32 + (int) (32 - (tempX - Math.floor(tempX / 32) * 32));
        performSlopeCollisions(World.RIGHT_SLOPE, 1, tempX, moveY);
    }

    private void performSlopeCollisions(int tileTypeNum, int dir, int tempX, int moveY) {
        int slopeTop = (int) Math.floor((y - 1) / 32) * 32;
        x += xspeed;

        if (World.getTileAtPoint((int) (tempX + xspeed) / 32, (int) (y + 63) / 32) == tileTypeNum
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
        if (xspeed < 0) {
            if (!World.isBlockAtPoint((int) Math.floor((x - 3) / 32), (int) Math.floor(y / 32))
                    && !World.isBlockAtPoint((int) Math.floor((x - 3) / 32), (int) Math.floor((y + 32) / 32))
                    && !World.isBlockAtPoint((int) Math.floor((x - 3) / 32), (int) Math.floor((y + 63) / 32))) {
                x += xspeed;
            } else {
                jump();
                xspeed = 0;
                int pos = (int) Math.floor(x / 32);
                if (!World.isBlockAtPoint(pos, (int) Math.floor(y / 32))
                        && !World.isBlockAtPoint(pos, (int) Math.floor((y + 32) / 32))
                        && !World.isBlockAtPoint(pos, (int) Math.floor((y + 63) / 32))) {
                    x = pos * 32;
                } else {
                    x = Math.round(x);
                }
            }
        }

        if (xspeed > 0) {
            if (!World.isBlockAtPoint((int) Math.floor((x + 3 + 32) / 32), (int) Math.floor(y / 32))
                    && !World.isBlockAtPoint((int) Math.floor((x + 3 + 32) / 32), (int) Math.floor((y + 32) / 32))
                    && !World.isBlockAtPoint((int) Math.floor((x + 3 + 32) / 32), (int) Math.floor((y + 63) / 32))) {
                x += xspeed;
            } else {
                jump();
                xspeed = 0;
                int pos = (int) Math.ceil(x / 32);
                if (!World.isBlockAtPoint(pos, (int) Math.floor(y / 32))
                        && !World.isBlockAtPoint(pos, (int) Math.floor((y + 32) / 32))
                        && !World.isBlockAtPoint(pos, (int) Math.floor((y + 63) / 32))) {
                    x = pos * 32;
                } else {
                    x = Math.round(x);
                }
            }
        }

        if (!World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + 64 + yspeed) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + 32 - 1) / 32), (int) Math.floor((y + 64 + yspeed) / 32))
                && !World.isBlockAtPoint((int) Math.floor(x / 32), (int) Math.floor((y + yspeed) / 32))
                && !World.isBlockAtPoint((int) Math.floor((x + 32 - 1) / 32), (int) Math.floor((y + yspeed) / 32))) {
            y += yspeed;
        } else {
            yspeed = 0;
            y = Math.round(y / 32) * 32;
        }
    }
    
    public void jump() {
        if (World.isBlockAtPoint((int) (x / 32), (int) ((y + 64) / 32))
                || World.isBlockAtPoint((int) ((x + 31) / 32), (int) ((y + 64) / 32))) {
            yspeed = JUMP_SPEED;
        }
    }
    
    public ZombieAI zombieAtPosition(int x, int y) {
        for (ZombieAI zombie : Enemy.zombieList) {
            if (zombie != this && zombie != null) {
                if (zombie.x <= x + 32 && zombie.x + 32 >= x
                        && zombie.y <= y + 64 && zombie.y + 32 >= y) {
                    return zombie;
                }
            }
        }
        
        return null;
    }
}
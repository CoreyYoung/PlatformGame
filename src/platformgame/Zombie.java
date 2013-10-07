package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Zombie {
    boolean awake = false;
    int id;
    float x;
    float y;
    static final int healthMAX = 10;
    public static final int defense = 2;
    public static final int stability = 0;
    int health = healthMAX;
    static final int damage = 5;
    static final int knockback = 3;
    static final int speedMAX = 3;
    static final int JUMPSPEED = -8;
    float xspeed = 0;
    float yspeed = 0;
    static float ACCELERATION = 0.1f;
    static Image sprite;
    
    Zombie(int x, int y, int id, boolean awake) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.awake = awake;
    }
    
    static void init() throws SlickException {
        sprite = new Image("data/graphics/zombieSprite.gif");
    }
    
    void update() {
        movement();
        collisions();
        
        if (health <= 0) {
            Enemy.zombie[id] = null;
        }
    }
    
    void render(int camX, int camY) {
        if (x <= Player.x) {
            sprite.draw(x + camX, y + camY);
        } else {
            sprite.draw((int)x + camX, (int)y + camY, 31, 0, 0, 64);
        }
    }
    
    void movement() {
        if (Player.x < x) {
            xspeed = Math.max(xspeed-ACCELERATION,-speedMAX);
        }
        else if (Player.x > x) {
            xspeed = Math.min(xspeed+ACCELERATION, speedMAX);
        }
        else {
            xspeed = 0;
        }
        
        if (Player.y+48 < y) {
            jump();
        }
        
        if (!World.tileAtPosition((int)Math.floor(x/32), (int)Math.floor((y+64)/32))
                && !World.tileAtPosition((int)Math.floor((x+31)/32), (int)Math.floor((y+64)/32))) {
            yspeed = Math.min(yspeed+World.GRAVITY, World.yspeedMAX);
        }
    }

    void collisions() {        
        if (xspeed != 0) {
            int destX = 0;
            int checkX = 0;
            if (xspeed < 0) {
                destX = (int) (x+xspeed);
                checkX = (int) ((x+xspeed)/32);
            }
            else if (xspeed > 0) {
                destX = (int) (x+xspeed+1);
                checkX = (int) ((x+32+xspeed)/32);
            }
            
            if (zombieAtPosition(destX, (int) y) != null) {
                //checks if other zombie is in front
                if (Math.signum(zombieAtPosition(destX, (int) y).x - x) == Math.signum(xspeed)) {
                    xspeed = 0;
                }
            }
            if (!World.tileAtPosition(checkX, (int)(y/32))
                    && !World.tileAtPosition(checkX, (int)((y+32)/32))
                    && !World.tileAtPosition(checkX, (int)((y+63)/32))) {
                x += xspeed;
            }
            else {
                xspeed = 0;
                x = Math.round(x/32)*32;
                jump();
            }
        }
        
        if (yspeed < 0) { 
            if (!World.tileAtPosition((int)(x/32), (int)((y+yspeed)/32))
                    && !World.tileAtPosition((int)((x+31)/32), (int)((y+yspeed)/32))) {
                y += yspeed;
            }
            else {
                yspeed = 0;
                y = Math.round(y/32)*32;
            }
        }
        else if (yspeed > 0) {
            if (!World.tileAtPosition((int)(x/32), (int)((y+63+yspeed)/32))
                    && !World.tileAtPosition((int)((x+31)/32), (int)((y+63+yspeed)/32))) {
                y += yspeed;
            }
            else {
                yspeed = 0;
                y = Math.round(y/32)*32;
            }
        }
    }
    
    void jump() {
        if (World.tileAtPosition((int)(x/32), (int)((y+64)/32))
                || World.tileAtPosition((int)((x+31)/32), (int)((y+64)/32))) {
            yspeed = JUMPSPEED;
        }
    }
    
    Zombie zombieAtPosition(int x, int y) {
        for (int i = 0; i < Enemy.zombieMAX; i ++) {
            if (Enemy.zombie[i] != null && i != id) {
                if (Enemy.zombie[i].x <= x+32 && Enemy.zombie[i].x+32 >= x
                        && Enemy.zombie[i].y <= y+64 && Enemy.zombie[i].y+32 >= y) {
                    return Enemy.zombie[i];
                }
            }
        }
        return null;
    }
}
package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Zombie {
    private static final int healthMAX = 10;
    private static final int speedMAX = 3;
    private static final int JUMPSPEED = -8;
    private static final float ACCELERATION = 0.1f;
    
    private final int id;
    private float yspeed = 0;
    private static Image sprite;
    
    public static final int defense = 2;
    public static final int stability = 0;
    public static final int damage = 5;
    public static final int knockback = 3;
    
    public float x;
    public float y;
    public float xspeed = 0;
    public boolean awake = false;
    public int health = healthMAX;
    
    
    public Zombie(int x, int y, int id, boolean awake) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.awake = awake;
    }
    
    public static void init() throws SlickException {
        sprite = new Image("data/graphics/zombieSprite.gif");
    }
    
    public void update() {
        movement();
        collisions();
        
        if (health <= 0) {
            Enemy.zombie[id] = null;
        }
    }
    
    public void render(int camX, int camY) {
        if (x <= Player.x) {
            sprite.draw(x + camX, y + camY);
        } else {
            sprite.draw((int)x + camX, (int)y + camY, 31, 0, 0, 64);
        }
    }
    
    private void movement() {
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
        
        if (!World.isBlockAtPoint((int)Math.floor(x/32), (int)Math.floor((y+64)/32))
                && !World.isBlockAtPoint((int)Math.floor((x+31)/32), (int)Math.floor((y+64)/32))) {
            yspeed = Math.min(yspeed+World.GRAVITY, World.yspeedMAX);
        }
    }

    private void collisions() {        
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
            if (!World.isBlockAtPoint(checkX, (int)(y/32))
                    && !World.isBlockAtPoint(checkX, (int)((y+32)/32))
                    && !World.isBlockAtPoint(checkX, (int)((y+63)/32))) {
                x += xspeed;
            }
            else {
                xspeed = 0;
                x = Math.round(x/32)*32;
                jump();
            }
        }
        
        if (yspeed < 0) { 
            if (!World.isBlockAtPoint((int)(x/32), (int)((y+yspeed)/32))
                    && !World.isBlockAtPoint((int)((x+31)/32), (int)((y+yspeed)/32))) {
                y += yspeed;
            }
            else {
                yspeed = 0;
                y = Math.round(y/32)*32;
            }
        }
        else if (yspeed > 0) {
            if (!World.isBlockAtPoint((int)(x/32), (int)((y+63+yspeed)/32))
                    && !World.isBlockAtPoint((int)((x+31)/32), (int)((y+63+yspeed)/32))) {
                y += yspeed;
            }
            else {
                yspeed = 0;
                y = Math.round(y/32)*32;
            }
        }
    }
    
    private void jump() {
        if (World.isBlockAtPoint((int)(x/32), (int)((y+64)/32))
               || World.isBlockAtPoint((int)((x+31)/32), (int)((y+64)/32))) {
            yspeed = JUMPSPEED;
        }
    }
    
    private Zombie zombieAtPosition(int x, int y) {
        for (Zombie zombie : Enemy.zombie) {
            if (zombie != null && id != zombie.id) {
                if (zombie.x <= x+32 && zombie.x+32 >= x
                        && zombie.y <= y+64 && zombie.y+32 >= y) {
                    return zombie;
                }
            }
        }
        return null;
    }
}
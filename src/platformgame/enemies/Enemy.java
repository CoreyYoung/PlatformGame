package platformgame.enemies;

import org.newdawn.slick.Image;

public abstract class Enemy {
    public int healthMAX;
    public int speedMAX;
    public float ACCELERATION;

    public Image sprite;

    public int defense;
    public int stability;
    public int damage;
    public int knockback;
    public int width;
    public int height;

    public float x;
    public float y;
    public float xspeed = 0;
    public float yspeed = 0;
    
    public boolean awake;
    public int health;
    
    public void update() {
        
    }
    
    public void render(int camX, int camY) {
        
    }
}
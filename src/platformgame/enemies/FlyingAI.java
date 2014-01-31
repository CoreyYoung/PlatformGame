package platformgame.enemies;

import org.newdawn.slick.Image;
import platformgame.Player;

public class FlyingAI extends Enemy {
    private final int MAX_SPEED;
    
    public float speed = 0;
    public int dir = 0;
    
    public FlyingAI (Image sprite, int x, int y, boolean awake, int healthMAX, int MAX_SPEED,
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
        
        dir = Math.max(dir, 0);
        dir = Math.min(dir, 360);
        
        speed = Math.min(MAX_SPEED, speed + ACCELERATION);
        
        final int rotationSpeed = 4;
        
        float tempDir = (int) ((Math.atan2(-(Player.x - x), Player.y - y)) * (180 / Math.PI) + 90);
        tempDir = Math.max(tempDir, 0);
        tempDir = Math.min(tempDir, 360);
        
        if (dir < tempDir) {
            dir += rotationSpeed;
        } else {
            dir -= rotationSpeed;
        }
        
        
        
        System.out.println("dir: " + dir + ", tempDir: " + tempDir);
        
        x += (float) (speed * Math.cos(dir * (Math.PI / 180)));
        y += (float) (speed * Math.sin(dir * (Math.PI / 180)));
        
    }
    
    @Override
    public void render(int camX, int camY) {
        sprite.draw(camX + x, camY + y);
    }
}









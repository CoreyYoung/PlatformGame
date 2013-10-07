package platformgame;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

abstract public class Camera {
    static int cameraWidth = Game.width;
    static int cameraHeight = Game.height;
    static int x = 0;
    static int y = 0;
    static int xOffset = 0;
    static int yOffset = 0;
    static final int yOffsetTimerMAX = 30;
    static int yOffsetTimer = yOffsetTimerMAX;
    
    static void update(Input input) {
        movement(input);
        wakeObjects();
    }
    
    static void render(Graphics g, Input input) {
        //render background
        World.render(-x, -y);
        
        //render foreground
        Enemy.render(-x, -y);
        Player.render(-x, -y, g);
        
        CombatSystem.render(input, -x, -y);
        
        //render HUD
        Hud.render(g, input);
    }
    
    static void movement(Input input) {
        x = inRange((int)Player.x-(cameraWidth/2), 0, World.levelWidth-cameraWidth);
        y = inRange((int)Player.y-(cameraHeight/2), 0, World.levelHeight-cameraHeight);
        //elasticCamera(input);
        
    }
    
    static void wakeObjects() {
        for (int i = 0; i < Enemy.zombieMAX; i ++) {
            if (Enemy.zombie[i] != null && ! Enemy.zombie[i].awake) {
                if (x <= Enemy.zombie[i].x+32 && x+cameraWidth >= Enemy.zombie[i].x
                        && y <= Enemy.zombie[i].y+64 && y+cameraHeight >= Enemy.zombie[i].y) {
                    Enemy.zombie[i].awake = true;
                }
            }
        }
    }
    
    static int inRange(int val, int min, int max) {
        val = Math.min(val, max);
        val = Math.max(val, min);
        return val;
    }
    
    static void elasticCamera(Input input) {
        x = (int)((Player.x+16)-(cameraWidth/2))+xOffset;
        y = (int)((Player.y+32)-(cameraHeight/2))+yOffset;
        
        x = inRange(x, 0, World.levelWidth-cameraWidth);
        y = inRange(y, 0, World.levelHeight-cameraHeight);
        
        if (input.isKeyDown(Input.KEY_UP)) {
            yOffsetTimer --;
            if (yOffsetTimer <= 0) {
                yOffset -= 2;
            }
        } else if (yOffset < 0) {
            yOffsetTimer = yOffsetTimerMAX;
            yOffset = Math.min(yOffset+4, 0);
        }
        
        if (input.isKeyDown(Input.KEY_DOWN)) {
            yOffsetTimer --;
            if (yOffsetTimer <= 0) {
                yOffset += 2;
            }
        } else if (yOffset > 0) {
            yOffsetTimer = yOffsetTimerMAX;
            yOffset = Math.max(yOffset-4, 0);
        }
        
        if (Player.dir == 0 && xOffset < 180) {
            xOffset += 3;
        } else if (Player.dir == 180 && xOffset > -180) {
            xOffset -= 3;
        }
        
        xOffset = inRange(xOffset, -(128), 128);
        yOffset = inRange(yOffset, -128, 128);  
    }
}
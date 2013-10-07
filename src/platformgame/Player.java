package platformgame;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import platformgame.inventory.*;

abstract public class Player {
    static int invincibilityTimerMAX = 60;
    static int invincibilityTimer = 0;
    static boolean invincible = false;
    static float x = 32;
    static float y = 384;
    static final int xspeedMAX = 3;
    static final int JUMPSPEED = -8;
    static final float ACCELERATION = 0.1f;
    static final float FRICTION = 0.05f;
    static float xspeed = 0;
    static float yspeed = 0;
    static Animation sprStand;
    static Animation sprWalk;
    static Animation sprHurt;
    static int dir = 0;
    static final int healthMAX = 10;
    static int health = healthMAX;
    static SpriteSheet spriteSheet;
    
    static void init() throws SlickException {
        spriteSheet = new SpriteSheet("data/graphics/player.gif", 32, 64);
        sprStand = new Animation(spriteSheet, 0, 0, 0, 0, false, 1000*1, true);
        sprWalk = new Animation(spriteSheet, 0, 0, 3, 0, true, (int)(1000*0.25), true);
        sprHurt = new Animation(spriteSheet, 0, 1, 3, 1, true, (int)(1000*0.1), true);
    }

    static void update(Input input) throws SlickException {
        movement(input);
        interact(input); //checks context sensitive 'interact' button (talk, pickup item, use door etc.)
        
        if (invincibilityTimer <= 0) {
            invincible = false;
        } else {
            invincibilityTimer --;
        }
        if (health <= 0) {
            //Restart game or something...
        }
    }
    static void render(int camX, int camY, Graphics g) {
        
        Animation sprite;
        if (invincible) {
            sprite = sprHurt;
        } else {
            if (xspeed == 0) {
                sprite = sprStand;
            } else {
                sprite = sprWalk;
            }
        }
        
        if (dir == 180) {
            sprite.draw((int)x+32+camX, y+camY, -32, 64);
        } else {
            sprite.draw((int)x+camX, y+camY);
        }
        
        int width = 32;
        int height = 16;
        if (Inventory.meleeSlot != null) {
            width = Inventory.getMeleeItem().width;
            height = Inventory.getMeleeItem().height;
        }
        if (dir == 180) {
            g.drawRect(x-width+camX, y+32-(height/2)+camY, width, height);
        } else {
            g.drawRect(x+32+width+camX, y+32-(height/2)+camY, -width, height);
        }
    }
    
    static void movement(Input input) {
        if (input.isKeyDown(Input.KEY_LEFT)) {
            if (input.isKeyPressed(Input.KEY_LEFT) || ! input.isKeyDown(Input.KEY_RIGHT)) {
                dir = 180;
            }
            if (xspeed > -xspeedMAX) {
                xspeed -= ACCELERATION;
            }
        } else {
            if (xspeed < 0) {
                xspeed = Math.min(xspeed+FRICTION, 0);
            }
        }
        
        if (input.isKeyDown(Input.KEY_RIGHT)) {
            if (input.isKeyPressed(Input.KEY_RIGHT) || ! input.isKeyDown(Input.KEY_LEFT)) {
                dir = 0;
            }
            if (xspeed < xspeedMAX) {
                xspeed += ACCELERATION;
            }
        } else {
            if (xspeed > 0) {
                xspeed = Math.max(xspeed-FRICTION, 0);
            }
        }
        if (input.isKeyPressed(Input.KEY_Z)) {
            boolean onGround = false;
            if (World.tileAtPosition((int)(x/32), (int)((y+64+1)/32))
                    && !World.tileAtPosition((int)(x/32), (int)((y+32+1)/32))) {
                onGround = true;
            }
            if (World.tileAtPosition((int)((x+32-1)/32), (int)((y+64+1)/32))
                    && !World.tileAtPosition((int)((x+32-1)/32), (int)((y+32+1)/32))) {
                onGround = true;
            }
            if (onGround) {
                yspeed = JUMPSPEED;
            }
        } else if (!input.isKeyDown(Input.KEY_Z)) {
            if (yspeed < 0) {
                yspeed *= 0.75;
            }
        }
        
        if (yspeed < World.yspeedMAX) {
            yspeed += World.GRAVITY;
        }
        
        collisions();
    }
    static void collisions() {
        if (!invincible) {
            for (int i = 0; i < Enemy.zombieMAX; i ++) {
                if (Enemy.zombie[i] != null) {
                    if (x < Enemy.zombie[i].x+32 && x+32 > Enemy.zombie[i].x
                            && y < Enemy.zombie[i].y+64 && y+64 > Enemy.zombie[i].y) {
                        xspeed = Math.signum(x-Enemy.zombie[i].x)
                                    *CombatSystem.calculateKnockback(Inventory.getStability(), Zombie.knockback);
                        Enemy.zombie[i].x -= Enemy.zombie[i].xspeed;
                        Enemy.zombie[i].xspeed = 0;
                        health -= CombatSystem.calculateDamage(Zombie.damage, Inventory.getDefense());
                        invincibilityTimer = invincibilityTimerMAX;
                        invincible = true;
                    }
                }
            }
        }
        if (xspeed < 0) {
            if (!World.tileAtPosition((int)Math.floor((x-3)/32), (int)Math.floor(y/32))
                    && !World.tileAtPosition((int)Math.floor((x-3)/32), (int)Math.floor((y+32)/32))
                    && !World.tileAtPosition((int)Math.floor((x-3)/32), (int)Math.floor((y+63)/32))) {
                x += xspeed;
            } else {
                xspeed = 0;
                int pos = (int)Math.floor(x/32);
                if (!World.tileAtPosition(pos, (int)Math.floor(y/32))
                        && !World.tileAtPosition(pos, (int)Math.floor((y+32)/32))
                        && !World.tileAtPosition(pos, (int)Math.floor((y+63)/32))) {
                    x = pos*32;
                } else {
                    x = Math.round(x);
                }
            }
        }
        
        if (xspeed > 0) {
            if (!World.tileAtPosition((int)Math.floor((x+3+32)/32), (int)Math.floor(y/32))
                    && !World.tileAtPosition((int)Math.floor((x+3+32)/32), (int)Math.floor((y+32)/32))
                    && !World.tileAtPosition((int)Math.floor((x+3+32)/32), (int)Math.floor((y+63)/32))) {
                x += xspeed;
            } else {
                xspeed = 0;
                int pos = (int)Math.ceil(x/32);
                if (!World.tileAtPosition(pos, (int)Math.floor(y/32))
                        && !World.tileAtPosition(pos, (int)Math.floor((y+32)/32))
                        && !World.tileAtPosition(pos, (int)Math.floor((y+63)/32))) {
                    x = pos*32;
                } else {
                    x = Math.round(x);
                }
            }
        }
        
        if (!World.tileAtPosition((int)Math.floor(x/32), (int)Math.floor((y+64+yspeed)/32))
                && !World.tileAtPosition((int)Math.floor((x+32-1)/32), (int)Math.floor((y+64+yspeed)/32))
                && !World.tileAtPosition((int)Math.floor(x/32), (int)Math.floor((y+yspeed)/32))
                && !World.tileAtPosition((int)Math.floor((x+32-1)/32), (int)Math.floor((y+yspeed)/32))) {
            y += yspeed;
        } else {
            yspeed = 0;
            y = Math.round(y/32)*32;
        }
    }
    
    static void interact(Input input) throws SlickException {
        if (input.isKeyPressed(Input.KEY_DOWN)) {
            for (int i = 0; i < World.doorMAX; i ++) {
                if (World.door[i] != null) {
                    if (x+32 >= World.door[i].x+16 && x <= World.door[i].x+16
                            && y+64 >= World.door[i].y && y <= World.door[i].y+64) {
                        openDoor(World.door[i]);
                        break;
                    }
                }
            }
            
            for (int i = 0; i < World.signMAX; i ++ ) {
                if (World.sign[i] != null) {
                    if (x+32 >= World.sign[i].x+16 && x <= World.sign[i].x+16
                            && y+64 >= World.sign[i].y && y <= World.sign[i].y+32) {
                        readSign(World.sign[i]);
                        break;
                    }
                }
            }
        }
    }
    
    static void openDoor(Door door) throws SlickException {
        xspeed = 0;
        yspeed = 0;
        String path = door.path;
        String levelName = World.levelName;
        World.init(path);
        for (int ii = 0; ii < World.doorMAX; ii ++) {
            if (World.door[ii] != null) {
                if (World.door[ii].path.equals(levelName)) {
                    x = World.door[ii].x;
                    y = World.door[ii].y;
                    break;
                }
            }
        }
    }
    
    static void readSign(Sign sign) {
        Hud.addMessage(sign.message);
    }
}
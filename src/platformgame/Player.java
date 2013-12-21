package platformgame;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

abstract public class Player {
    private static final int xspeedMAX = 3;
    private static final int JUMPSPEED = -8;
    private static final float ACCELERATION = 0.1f;
    private static final float FRICTION = 0.07f;
    private static float yspeed = 0;
    private static Animation sprStand;
    private static Animation sprWalk;
    private static Animation sprHurt;
    private static SpriteSheet spriteSheet;
    
    public static final int invincibilityTimerMAX = 60;
    public static int invincibilityTimer = 0;
    public static boolean invincible = false;
    public static final int healthMAX = 10;
    public static int health = healthMAX;
    public static int dir = 0;
    public static float x = 32;
    public static float y = 384;
    public static float xspeed = 0;
    
    public static void init() throws SlickException {
        spriteSheet = new SpriteSheet("data/graphics/player.gif", 32, 64);
        sprStand = new Animation(spriteSheet, 0, 0, 0, 0, false, 1000*1, true);
        sprWalk = new Animation(spriteSheet, 0, 0, 3, 0, true, (int)(1000*0.25), true);
        sprHurt = new Animation(spriteSheet, 0, 1, 3, 1, true, (int)(1000*0.1), true);
    }

    public static void update(Input input) throws SlickException {
        movement(input);
        interact(input); //checks context sensitive 'interact' button (talk, pickup item, use door etc.)
        
        if (invincibilityTimer <= 0) {
            invincible = false;
        } else {
            invincibilityTimer --;
        }
    }
    
    public static void render(int camX, int camY, Graphics g) {
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
    }
    
    private static void movement(Input input) {
        if (input.isKeyDown(Input.KEY_A)) {
            if (input.isKeyPressed(Input.KEY_A) || ! input.isKeyDown(Input.KEY_D)) {
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
        
        if (input.isKeyDown(Input.KEY_D)) {
            if (input.isKeyPressed(Input.KEY_D) || ! input.isKeyDown(Input.KEY_A)) {
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
        
        if (input.isKeyPressed(Input.KEY_SPACE)) {
            if ((World.isBlockAtPoint((int)(x/32), (int)((y+64+1)/32))
                    && !World.isBlockAtPoint((int)(x/32), (int)((y+32+1)/32)))
                    
            || (World.isBlockAtPoint((int)((x+32-1)/32), (int)((y+64+1)/32))
                    && !World.isBlockAtPoint((int)((x+32-1)/32), (int)((y+32+1)/32)))
                    
            || (World.getTileAtPoint((int) (x+31)/32, (int) (y+64)/32) == 2
                    || World.getTileAtPoint((int) x/32, (int) (y+64)/32) == 3)) {
                
                yspeed = JUMPSPEED;
            }
        } else if (!input.isKeyDown(Input.KEY_SPACE)) {
            if (yspeed < 0) {
                yspeed *= 0.75;
            }
        }
        
        if (yspeed < World.yspeedMAX) {
            yspeed += World.GRAVITY;
        }
        
        collisions();
    }
    
    private static void collisions() {
        
        
        if (onSlope(World.LEFT_SLOPE)) {
            performLeftSlopeCollision();
        } else if (onSlope(World.RIGHT_SLOPE)) {
            performRightSlopeCollision();
        } else {
            performBlockCollisions();
        }
    }
    
    private static boolean onSlope(int tileTypeNum) {
        final int x1 = (int) x+31;
        final int x2 = (int) x+1;
        
        return (World.getTileAtPoint((int) x1/32, (int) (y+63)/32) == tileTypeNum
                || World.getTileAtPoint((int) x2/32, (int) (y+63)/32) == tileTypeNum);
    }
    
    private static void performLeftSlopeCollision() {
        int tempX = (int) x+1;
        int moveY = (int) Math.floor((y-1)/32)*32 + (int) (tempX-Math.floor(tempX/32)*32);
        performSlopeCollisions(World.LEFT_SLOPE, -1, tempX, moveY);
    }
    
    private static void performRightSlopeCollision() {
        int tempX = (int) x+31;
        int moveY = (int) Math.floor((y-1)/32)*32 + (int) (32-(tempX-Math.floor(tempX/32)*32));
        performSlopeCollisions(World.RIGHT_SLOPE, 1, tempX, moveY);
    }
    
    private static void performSlopeCollisions(int tileTypeNum, int dir, int tempX, int moveY) {
        int slopeTop = (int) Math.floor((y-1)/32)*32;
        x += xspeed;
        
        if (World.getTileAtPoint((int) (tempX+xspeed)/32, (int) (y+63)/32) == tileTypeNum
                || Math.signum(xspeed) != dir) {
            if (y > moveY) {
                y = moveY;
            }
        } else {
            y = slopeTop;
        }

        if (y+yspeed <= moveY) {
            y += yspeed;
        } else {
            yspeed = 0;
            y = moveY;
        }
        
        if (World.isBlockAtPoint((int) (x+32)/32, (int) y/32)) {
            x = (int) (x/32)*32;
        } else if (World.isBlockAtPoint((int) (x)/32, (int) y/32)) {
            x = (int) ((x+32)/32)*32;
        }
    }
    
    private static void performBlockCollisions() {
        if (xspeed < 0) {
            if (!World.isBlockAtPoint((int)Math.floor((x-3)/32), (int)Math.floor(y/32))
                    && !World.isBlockAtPoint((int)Math.floor((x-3)/32), (int)Math.floor((y+32)/32))
                    && !World.isBlockAtPoint((int)Math.floor((x-3)/32), (int)Math.floor((y+63)/32))) {
                x += xspeed;
            } else {
                xspeed = 0;
                int pos = (int)Math.floor(x/32);
                if (!World.isBlockAtPoint(pos, (int)Math.floor(y/32))
                        && !World.isBlockAtPoint(pos, (int)Math.floor((y+32)/32))
                        && !World.isBlockAtPoint(pos, (int)Math.floor((y+63)/32))) {
                    x = pos*32;
                } else {
                    x = Math.round(x);
                }
            }
        }

        if (xspeed > 0) {
            if (!World.isBlockAtPoint((int)Math.floor((x+3+32)/32), (int)Math.floor(y/32))
                    && !World.isBlockAtPoint((int)Math.floor((x+3+32)/32), (int)Math.floor((y+32)/32))
                    && !World.isBlockAtPoint((int)Math.floor((x+3+32)/32), (int)Math.floor((y+63)/32))) {
                x += xspeed;
            } else {
                xspeed = 0;
                int pos = (int)Math.ceil(x/32);
                if (!World.isBlockAtPoint(pos, (int)Math.floor(y/32))
                        && !World.isBlockAtPoint(pos, (int)Math.floor((y+32)/32))
                        && !World.isBlockAtPoint(pos, (int)Math.floor((y+63)/32))) {
                    x = pos*32;
                } else {
                    x = Math.round(x);
                }
            }
        }

        if (!World.isBlockAtPoint((int)Math.floor(x/32), (int)Math.floor((y+64+yspeed)/32))
                && !World.isBlockAtPoint((int)Math.floor((x+32-1)/32), (int)Math.floor((y+64+yspeed)/32))
                && !World.isBlockAtPoint((int)Math.floor(x/32), (int)Math.floor((y+yspeed)/32))
                && !World.isBlockAtPoint((int)Math.floor((x+32-1)/32), (int)Math.floor((y+yspeed)/32))) {
            y += yspeed;
        } else {
            yspeed = 0;
            y = Math.round(y/32)*32;
        }
    }
    
    private static void interact(Input input) throws SlickException {
        if (input.isKeyPressed(Input.KEY_S)) {
            for (Door door : World.door) {
                if (door != null) {
                    if (x+32 >= door.x+16 && x < door.x+16
                            && y+64 >= door.y && y <= door.y+64) {
                        openDoor(door.path);
                        break;
                    }
                }
            }
            
            for (Sign sign : World.sign) {
                if (sign != null) {
                    if (x+32 >= sign.x+16 && x <= sign.x+16
                            && y+64 >= sign.y && y <= sign.y+32) {
                        readSign(sign);
                        break;
                    }
                }
            }
            
            for (Chest chest : Chest.chestList) {
                if (chest != null) {
                    if (x+32 >= chest.x+16 && x <= chest.x+16
                            && y+64 >= chest.y && y <= chest.y+32) {
                        useChest(chest);
                        break;
                    }
                }
            }
            
            for (FloppyDisk floppyDisk : FloppyDisk.floppyDiskList) {
                if (floppyDisk != null) {
                    if (x+32 >= floppyDisk.x+16 && x <= floppyDisk.x+16
                            && y+64 >= floppyDisk.y && y <= floppyDisk.y+32) {
                        floppyDisk.useFloppyDisk();
                        break;
                    }
                }
            }
        }
    }
    
    private static void openDoor(String path) throws SlickException {
        String levelDeparting = World.levelName;
        World.init(path);
        
        xspeed = 0;
        yspeed = 0;
        
        for (Door door : World.door) {
            if (door != null) {
                if (door.path.equals(levelDeparting)) {
                    x = door.x;
                    y = door.y;
                    break;
                }
            }
        }
    }
    
    private static void readSign(Sign sign) {
        Hud.addMessage(sign.message);
    }
    
    private static void useChest(Chest chest) throws SlickException {
        chest.useChest();
    }
}
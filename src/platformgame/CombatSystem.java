package platformgame;

import java.util.Iterator;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import platformgame.inventory.AmmoItem;
import platformgame.inventory.Inventory;
import platformgame.inventory.RangedItem;

abstract public class CombatSystem {
    public static void update(Input input) {
        if (input.isKeyPressed(Input.KEY_X)) {
            useMelee();
        }
        if (input.isKeyPressed(Input.KEY_C)) {
            useRanged(input);
        }
        
        performCollisions();
    }
    
    public static void render(Input input, int camX, int camY) {
        if (input != null) {
            if (input.isKeyDown(Input.KEY_X)) {
                if (Inventory.getMeleeItem() != null) {
                    Inventory.getMeleeItem().renderSprite((int) Player.x+camX, (int) Player.y+camY, Player.dir);
                }
            }
        }
    }
    
    public static int calculateDamage(int attack, int defense) {
        return Math.max(attack-defense, 1);
    }
    
    public static int calculateKnockback(int stability, int knockback) {
        return Math.max(knockback-stability, 0);
    }
    
    public static void useMelee() {
        //create collision box
        int boxWidth = 32;
        int boxHeight = 16;
        int knockback = 2;
        if (Inventory.getMeleeItem() != null) {
            boxWidth = Inventory.getMeleeItem().width;
            boxHeight = Inventory.getMeleeItem().height;
            knockback = Inventory.getMeleeItem().knockback;
        }
        
        int x1, x2, y1, y2;
        if (Player.dir == 0) {
            x1 = (int) (Player.x+32+boxWidth);
            x2 = (int) (Player.x);
        } else {
            x1 = (int) (Player.x);
            x2 = (int) (Player.x-boxWidth);
        }
        y1 = (int) (Player.y+32+(boxHeight/2));
        y2 = (int) (Player.y+32-(boxHeight/2));
        
        //perform collisions
        for (Zombie zombie : Enemy.zombie) {
            if (zombie != null) {
                if (zombie.x < x1 && zombie.x+32 > x2 && zombie.y < y1 && zombie.y+64 > y2) {
                    zombie.health -= calculateDamage(Inventory.getAttack(), Zombie.defense);
                    if (Player.x < zombie.x) {
                        zombie.xspeed += calculateKnockback(Zombie.stability, knockback);
                    } else {
                        zombie.xspeed -= calculateKnockback(Zombie.stability, knockback);
                    }
                }
            }
        }
    }
    
    public static void useRanged(Input input) {
        if (Inventory.getRangedItem() != null && Inventory.getAmmoItem() != null) {
            int dir;
            if (input.isKeyDown(Input.KEY_UP)) {
                if (input.isKeyDown(Input.KEY_LEFT)) {
                    dir = 180+45;
                } else if (input.isKeyDown(Input.KEY_RIGHT)) {
                    dir = 360-45;
                } else {
                    dir = 270;
                }
            } else if (input.isKeyDown(Input.KEY_DOWN)) {
                if (input.isKeyDown(Input.KEY_LEFT)) {
                    dir = 180-45+10;
                } else if (input.isKeyDown(Input.KEY_RIGHT)) {
                    dir = 0+45-10;
                } else {
                    dir = 90;
                }
            } else if (Player.dir == 180) {
                dir = 180+10;
            } else {
                dir = 360-10;
            }
            
            int x = (int) Player.x;
            int y = (int) Player.y+16;
            int speed = 16;
            RangedItem ranged = Inventory.getRangedItem();
            AmmoItem ammo = Inventory.getAmmoItem();
            Projectile.createProjectile(x, y, speed, dir, ranged, ammo);
            Inventory.getFirstSlot(ammo).itemStack.amount--;
        }
    }
    
    static void performCollisions() {
        for (Zombie zombie : Enemy.zombie) {
            if (zombie != null) {
                Iterator iterator = Projectile.projectileList.iterator();
                while (iterator.hasNext()) {
                    Projectile projectile = (Projectile) iterator.next();
                    
                    Rectangle zombieHitBox = new Rectangle(zombie.x, zombie.y, 32, 64);
                    
                    if (projectile.hitBox.intersects(zombieHitBox)) {
                        iterator.remove();
                        zombie.health -= calculateDamage(projectile.damage, Zombie.defense);
                    }
                }
            }
        }
    }
}
package platformgame;

import java.util.Iterator;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import platformgame.inventory.AmmoItem;
import platformgame.inventory.Inventory;
import platformgame.inventory.Item;
import platformgame.inventory.RangedItem;

abstract public class CombatSystem {
    private static int attackTimer = 0;
    private static Item attackItem;
    
    public static void update(Input input) {
        if (attackTimer <= 0) {
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                useMelee();
            }
            
            if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
                useRanged(input);
            }
        } else {
            attackTimer --;
        }
        
        performCollisions();
    }
    
    public static void render(Input input, int camX, int camY) {
        if (attackTimer > 0) {
            if (attackItem != null) {
                attackItem.renderSprite((int) Player.x + camX, (int) Player.y + camY, Player.dir);
            }
        }
    }
    
    private static int calculateDamage(int attack, int defense) {
        return Math.max(attack-defense, 1);
    }
    
    private static int calculateKnockback(int stability, int knockback) {
        return Math.max(knockback-stability, 0);
    }
    
    private static void useMelee() {
        attackItem = Inventory.getMeleeItem();
        attackTimer = 30;
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
                        zombie.xspeed = calculateKnockback(Zombie.stability, knockback);
                    } else {
                        zombie.xspeed = -calculateKnockback(Zombie.stability, knockback);
                    }
                }
            }
        }
    }
    
    private static void useRanged(Input input) {
        if (Inventory.getRangedItem() != null && Inventory.getAmmoItem() != null) {
            int mouseX = input.getMouseX()+Camera.x;
            int mouseY = input.getMouseY()+Camera.y;
            
            int x = (int) Player.x;
            int y = (int) Player.y+16;
            int speed = 16;
            int dir = (int) Math.toDegrees(Math.atan2(-(mouseX-x), mouseY-y))+90;
            RangedItem ranged = Inventory.getRangedItem();
            AmmoItem ammo = Inventory.getAmmoItem();
            Projectile.createProjectile(x, y, speed, dir, ranged, ammo);
            Inventory.getFirstSlot(ammo).itemStack.amount--;
            
            attackTimer = ranged.speed;
            attackItem = ranged;
        }
    }
    
    private static void performCollisions() {
        if (! Player.invincible) {
            for (Zombie zombie : Enemy.zombie) {
                if (zombie != null) {
                    if (Player.x < zombie.x+32 && Player.x+32 > zombie.x
                            && Player.y < zombie.y+64 && Player.y+64 > zombie.y) {
                        Player.xspeed = Math.signum(Player.x-zombie.x)
                                    *CombatSystem.calculateKnockback(Inventory.getStability(), Zombie.knockback);
                        zombie.x -= zombie.xspeed;
                        zombie.xspeed = 0;
                        Player.health -= CombatSystem.calculateDamage(Zombie.damage, Inventory.getDefense());
                        Player.invincibilityTimer = Player.invincibilityTimerMAX;
                        Player.invincible = true;
                    }
                }
            }
        }
        
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
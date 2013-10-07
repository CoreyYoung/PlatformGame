package platformgame;

import org.newdawn.slick.Input;
import platformgame.inventory.Inventory;

abstract public class CombatSystem {
    public static void update(Input input) {
        if (input.isKeyPressed(Input.KEY_X)) {
            useMelee();
        }
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
            System.out.println(knockback);
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
        for (int i = 0; i < Enemy.zombieMAX; i ++) {
            if (Enemy.zombie[i] != null) {
                if (Enemy.zombie[i].x < x1 && Enemy.zombie[i].x+32 > x2
                        && Enemy.zombie[i].y < y1 && Enemy.zombie[i].y+64 > y2) {
                    Enemy.zombie[i].health -= calculateDamage(Inventory.getAttack(), Zombie.defense);
                    if (Player.x < Enemy.zombie[i].x) {
                        Enemy.zombie[i].xspeed += calculateKnockback(Zombie.stability, knockback);
                    } else {
                        Enemy.zombie[i].xspeed -= calculateKnockback(Zombie.stability, knockback);
                    }
                }
            }
        }
    }
}

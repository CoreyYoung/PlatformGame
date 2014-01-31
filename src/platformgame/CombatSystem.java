package platformgame;

import platformgame.enemies.EnemyHandler;
import java.util.Iterator;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import platformgame.enemies.Enemy;
import platformgame.enemies.FlyingAI;
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
            attackTimer--;
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
        int damage = Math.max(attack - defense, 1);
        System.out.println(damage);
        return damage;
    }

    private static float calculateKnockback(int stability, float knockback) {
        return Math.max(knockback - stability, 0);
    }

    private static void useMelee() {
        attackItem = Inventory.getMeleeItem();
        attackTimer = 30;

        //create collision box
        int boxWidth = 32;
        int boxHeight = 16;
        float knockback = 2;
        if (Inventory.getMeleeItem() != null) {
            boxWidth = Inventory.getMeleeItem().width;
            boxHeight = Inventory.getMeleeItem().height;
            knockback = Inventory.getMeleeItem().knockback;
            attackTimer = Inventory.getMeleeItem().speed;
        }

        int x1, x2, y1, y2;
        if (Player.dir == 0) {
            x1 = (int) (Player.x + 32 + boxWidth);
            x2 = (int) (Player.x);
        } else {
            x1 = (int) (Player.x);
            x2 = (int) (Player.x - boxWidth);
        }
        y1 = (int) (Player.y + 32 + (boxHeight / 2));
        y2 = (int) (Player.y + 32 - (boxHeight / 2));

        //perform collisions
        for (Enemy enemy : EnemyHandler.enemyList) {
            if (enemy != null) {
                if (enemy.x < x1 && enemy.x + 32 > x2 && enemy.y < y1 && enemy.y + 64 > y2) {
                    enemy.health -= calculateDamage(Inventory.getAttack(), enemy.defense);
                    if (Player.x < enemy.x) {
                        enemy.xspeed = calculateKnockback(enemy.stability, knockback);
                    } else {
                        enemy.xspeed = -calculateKnockback(enemy.stability, knockback);
                    }
                }
            }
        }
    }

    private static void useRanged(Input input) {
        if (Inventory.getRangedItem() != null && Inventory.getAmmoItem() != null) {
            int mouseX = input.getMouseX() + Camera.x;
            int mouseY = input.getMouseY() + Camera.y;

            int x = (int) Player.x;
            int y = (int) Player.y + 16;
            int speed = 16;
            int dir = (int) Math.toDegrees(Math.atan2(-(mouseX - x), mouseY - y)) + 90;
            RangedItem ranged = Inventory.getRangedItem();
            AmmoItem ammo = Inventory.getAmmoItem();
            Projectile.createProjectile(x, y, speed, dir, ranged, ammo);
            Inventory.getFirstSlot(ammo).itemStack.amount--;

            attackTimer = ranged.speed;
            attackItem = ranged;
        }
    }

    private static void performCollisions() {
        if (!Player.invincible) {
            for (Enemy enemy : EnemyHandler.enemyList) {
                if (enemy != null) {
                    if (Player.x < enemy.x + enemy.width && Player.x + 32 > enemy.x
                            && Player.y < enemy.y + enemy.height && Player.y + 64 > enemy.y) {
                        
//                        Player.xspeed = Math.signum(Player.x - zombie.x)
//                                * CombatSystem.calculateKnockback(Inventory.getStability(), zombie.knockback);
                        
                        float speed = calculateKnockback(Inventory.getStability(), enemy.knockback);
                        int dir = (int) Math.toDegrees(Math.atan2(-(Player.x - enemy.x), Player.y - enemy.y)) + 90;
                        
                        Player.xspeed = (float) (speed * Math.cos(dir * (Math.PI / 180)));
                        Player.yspeed = (float) (speed * Math.sin(dir * (Math.PI / 180)));
                        
                        System.out.println("Speed: " + speed);
                        System.out.println("Xspeed: " + Player.xspeed + ", Yspeed: " + Player.yspeed + ".");
                        
                        enemy.x -= enemy.xspeed;
                        enemy.xspeed = 0;
                        Player.health -= CombatSystem.calculateDamage(enemy.damage, Inventory.getDefense());
                        Player.invincibilityTimer = Player.invincibilityTimerMAX;
                        Player.invincible = true;
                    }
                }
            }
        }

        for (Enemy enemy : EnemyHandler.enemyList) {
            if (enemy != null) {
                Iterator iterator = Projectile.projectileList.iterator();
                while (iterator.hasNext()) {
                    Projectile projectile = (Projectile) iterator.next();

                    Rectangle zombieHitBox = new Rectangle(enemy.x, enemy.y, enemy.width, enemy.height);

                    if (projectile.hitBox.intersects(zombieHitBox)) {
                        iterator.remove();
                        enemy.health -= calculateDamage(projectile.damage, enemy.defense);
                        
                        int dir = (int) Math.signum(Player.x-enemy.x);
                        if (enemy.getClass().getCanonicalName().equals("platformgame.enemies.FlyingAI")) {
                            FlyingAI flyingAI = (FlyingAI) enemy;
                            //flyingAI.dir = (int) Math.toDegrees(Math.atan2(-(enemy.x - projectile.x), enemy.y - projectile.y)) + 90;
                            flyingAI.dir = (int) projectile.dir;
                            flyingAI.speed = calculateKnockback(enemy.stability, projectile.knockback);
                        }
                        
                        if (Math.signum(enemy.xspeed) == dir) {
                            enemy.xspeed += -dir*calculateKnockback(enemy.stability, projectile.knockback);
                        } else {
                            enemy.xspeed = -dir*calculateKnockback(enemy.stability, projectile.knockback);
                        }
                    }
                }
            }
        }
    }
}
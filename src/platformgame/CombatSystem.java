package platformgame;

import java.util.Iterator;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import platformgame.enemies.Enemy;
import platformgame.enemies.EnemyHandler;
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
                attackItem.renderSprite((int) StatePlaying.player.x + camX, (int) StatePlaying.player.y + camY, (int) StatePlaying.player.dir);
            }
        }
    }

    private static int calculateDamage(int attack, int defense) {
        int damage = Math.max(attack - defense, 1);
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
            x1 = (int) (StatePlaying.player.x + 32 + boxWidth);
            x2 = (int) (StatePlaying.player.x);
        } else {
            x1 = (int) (StatePlaying.player.x);
            x2 = (int) (StatePlaying.player.x - boxWidth);
        }
        y1 = (int) (StatePlaying.player.y + 32 + (boxHeight / 2));
        y2 = (int) (StatePlaying.player.y + 32 - (boxHeight / 2));

        //perform collisions
        for (Enemy enemy : EnemyHandler.enemyList) {
            if (enemy != null) {
                if (enemy.x < x1 && enemy.x + 32 > x2 && enemy.y < y1 && enemy.y + 64 > y2) {
                    enemy.health -= calculateDamage(Inventory.getAttack(), enemy.defense);

                    float magnitude = calculateKnockback(enemy.stability, knockback);
                    float dir;

                    if (StatePlaying.player.x < enemy.x) {
                        dir = 0;
                    } else {
                        dir = 180;
                    }

                    enemy.velocity = new Vector(dir, magnitude);
                }
            }
        }
    }

    private static void useRanged(Input input) {
        if (Inventory.getRangedItem() != null && Inventory.getAmmoItem() != null) {
            int mouseX = input.getMouseX() + Camera.x;
            int mouseY = input.getMouseY() + Camera.y;

            int x = (int) StatePlaying.player.x;
            int y = (int) StatePlaying.player.y + 16;
            int speed = 16;
            int dir = (int) Math.toDegrees(Math.atan2(-(mouseX - x), mouseY - y)) + 90;
            RangedItem ranged = Inventory.getRangedItem();
            AmmoItem ammo = Inventory.getAmmoItem();
            Projectile.createProjectile(x, y, speed, dir, ranged, ammo);
            Inventory.getFirstSlot(ammo).itemStack.amount--;

            attackTimer = ranged.speed;
            attackItem = ranged;

            ranged.bowSound.play();
        }
    }

    private static void performCollisions() {
        if (!Player.invincible) {
            for (Enemy enemy : EnemyHandler.enemyList) {
                if (enemy != null) {
                    if (StatePlaying.player.x < enemy.x + enemy.width && StatePlaying.player.x + 32 > enemy.x
                            && StatePlaying.player.y < enemy.y + enemy.height && StatePlaying.player.y + 64 > enemy.y) {
                        float speed = calculateKnockback(Inventory.getStability(), enemy.knockback);
                        int dir = (int) Math.toDegrees(Math.atan2(-(StatePlaying.player.x - enemy.x), StatePlaying.player.y - enemy.y)) + 90;

                        StatePlaying.player.velocity = new Vector(dir, speed);

                        enemy.x -= enemy.velocity.getXMagnitude();
                        enemy.velocity = new Vector(Vector.getDir(0, 0, 0, enemy.velocity.getYMagnitude()), enemy.velocity.getYMagnitude());

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

                        Vector knockback = new Vector((float) projectile.dir, calculateKnockback(enemy.stability, projectile.knockback));
                        enemy.velocity = knockback;
                    }
                }
            }
        }
    }
}

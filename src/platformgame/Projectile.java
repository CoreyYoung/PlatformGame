package platformgame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.Graphics;
import platformgame.inventory.AmmoItem;
import platformgame.inventory.RangedItem;

public class Projectile {

    private double x;
    private double y;
    private double speed;
    private double dir;
    private int width;
    private int length;

    public int damage;
    public float knockback;
    public HitBox hitBox;
    public AmmoItem ammo;
    public static List<Projectile> projectileList = new ArrayList<>();

    public static void update() {
        Iterator iterator = projectileList.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = (Projectile) iterator.next();
            projectile.moveProjectile();
            projectile.updateHitBox();
            if (!World.isPointInLevel((int) projectile.x, (int) projectile.y)
                    || projectile.hitBox.collisionWithWorld()) {
                iterator.remove();
            }
        }
    }

    private void moveProjectile() {
        applyGravity();
        x += speed * Math.cos(dir * (Math.PI / 180));
        y += (speed * Math.sin(dir * (Math.PI / 180)));
    }

    private void applyGravity() {
        if ((int) dir == 270) {
            speed -= World.GRAVITY;
            if (speed < 0) {
                dir = 90;
                speed = 0;
            }
        } else if ((int) dir == 90) {
            speed += World.GRAVITY;
        } else {
            //add gravity to cartesian coordinates
            double tempX, tempY;
            tempX = (speed * Math.cos(dir * (Math.PI / 180)));
            tempY = (speed * Math.sin(dir * (Math.PI / 180))) + World.GRAVITY;
            //update speed based on updated cartesian cordinates
            speed = Math.sqrt(tempX * tempX + tempY * tempY);
            //update direction based on updated cartesian coordinates
            dir = Math.atan(tempY / tempX) * (180 / Math.PI);
            if (tempX < 0) {
                dir += 180;
            } else {
                if (tempY >= 0) {
                    dir += 360;
                }
            }
        }
    }

    private void updateHitBox() {
        hitBox.update((int) x + length / 2 - width, (int) y - width / 2);
    }

    public void render(int camX, int camY, Graphics g) {
        ammo.renderSprite((int) (x - width / 2) + camX, (int) (y - width / 2) + camY, (int) dir);
        hitBox.render(camX, camY, g);
    }

    public static void createProjectile(int x, int y, int speed, int dir, RangedItem ranged, AmmoItem ammo) {
        Projectile projectile = new Projectile();
        Projectile.projectileList.add(projectile);

        projectile.x = x;
        projectile.y = y;
        projectile.speed = speed;
        projectile.dir = dir;
        projectile.damage = (ranged.attack + ammo.attack) / 2;
        projectile.knockback = ammo.knockback;
        projectile.width = ammo.sprite.getHeight();
        projectile.length = ammo.sprite.getWidth();
        projectile.ammo = ammo;

        projectile.hitBox = HitBox.createHitbox((int) x + projectile.length / 2 - projectile.width,
                (int) y - projectile.width / 2, projectile.width, projectile.width);
    }
}

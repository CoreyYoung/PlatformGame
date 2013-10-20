package platformgame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import platformgame.inventory.AmmoItem;
import platformgame.inventory.RangedItem;

public class Projectile {
    public double x;
    public double y;
    public double speed;
    public double dir;
    public int damage;
    public Image sprite;
    public int width;
    Rectangle hitBox;
    public static List<Projectile> projectileList = new ArrayList<>();
    
    public static void update() {        
        Iterator iterator = Projectile.projectileList.iterator();
        
        while(iterator.hasNext()) {
            Projectile projectile = (Projectile) iterator.next();
            
            projectile.moveProjectile();
            projectile.updateHitBox();
            if (! World.isPointInLevel((int) projectile.x, (int) projectile.y)
                    || projectile.collisionWithWall()) {
                iterator.remove();
            }
        }
    }
    
    private void moveProjectile() {
        applyGravity();
        x += speed * Math.cos(dir*(Math.PI/180));
        y += (speed * Math.sin(dir*(Math.PI/180)));
    }
    
    private void applyGravity() {
        //System.out.println(dir);
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
            tempX = (speed * Math.cos(dir*(Math.PI/180)));
            tempY = (speed * Math.sin(dir*(Math.PI/180))) + World.GRAVITY;
            //update speed based on updated cartesian cordinates
            speed = Math.sqrt(tempX*tempX + tempY*tempY);        
            //update direction based on updated cartesian coordinates
            dir = Math.atan(tempY/tempX)*(180/Math.PI);
            if (tempX < 0) {
                dir += 180;
            } else {
                if (tempY >= 0) {
                    dir += 360;
                }
            }
        }
    }
    
    private boolean collisionWithWall() {
        int x1 = (int) hitBox.getMinX()/32;
        int y1 = (int) hitBox.getMinY()/32;
        int x2 = (int) hitBox.getMaxX()/32;
        int y2 = (int) hitBox.getMaxY()/32;
        
        return (World.tileAtPosition(x1, y1) || World.tileAtPosition(x1, y2)
                || World.tileAtPosition(x2, y1) || World.tileAtPosition(x2, y2));
    }
    
    private void updateHitBox() {
        hitBox = new Rectangle((float) x+sprite.getWidth()/2-width, (float) y-(width/2), width, width);
    }
    
    public void render(int camX, int camY, Graphics g) {
        sprite.setRotation((int) dir);
        sprite.draw((int) (x-width/2)+camX, (int) (y-width/2)+camY);
        g.drawRect(hitBox.getMinX()+camX, hitBox.getMinY()+camY, hitBox.getWidth(), hitBox.getHeight());
    }
    
    public static void createProjectile(int x, int y, int speed, int dir, RangedItem ranged, AmmoItem ammo) {
        Projectile projectile = new Projectile();
        Projectile.projectileList.add(projectile);
        
        projectile.x = x;
        projectile.y = y;
        projectile.speed = speed;
        projectile.dir = dir;
        projectile.sprite = ammo.sprite;
        projectile.damage = (ranged.attack+ammo.attack)/2;
        projectile.width = projectile.sprite.getHeight();
        
        projectile.updateHitBox();
    }
}
package platformgame;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class CollisionBox extends Rectangle {

    private CollisionBox(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    public void update(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(int camX, int camY, Graphics g) {
        g.drawRect(x + camX, y + camY, width, height);
    }

    public static CollisionBox createHitbox(int x, int y, int width, int height) {
        CollisionBox hitBox = new CollisionBox(x, y, width, height);
        return hitBox;
    }

    public boolean collisionWithWorld() {
        int x1 = (int) x / 32;
        int y1 = (int) y / 32;
        int x2 = (int) (x + width) / 32;
        int y2 = (int) (y + height) / 32;

        return (World.isBlockInLine(x1, x2, y1, y1) || World.isBlockInLine(x1, x2, y2, y2)
                || World.isBlockInLine(x1, x1, y1, y2) || World.isBlockInLine(x2, x2, y1, y2));
    }
}

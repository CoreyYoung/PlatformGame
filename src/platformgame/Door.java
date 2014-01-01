package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Door {

    private static Image sprite;

    public int x;
    public int y;
    public String path;

    public Door(int x, int y, String path) {
        this.x = x;
        this.y = y;
        this.path = path;
    }

    public static void init() throws SlickException {
        sprite = new Image("data/graphics/world objects/Door.gif");
    }

    public void render(int camX, int camY) {
        sprite.draw(x + camX, y + camY);
    }
}

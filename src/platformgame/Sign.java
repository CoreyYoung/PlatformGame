package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Sign {

    private static Image sprite;

    public int x;
    public int y;
    public String message;

    public Sign(int x, int y, String message) {
        this.x = x;
        this.y = y;
        this.message = message;
    }

    public static void init() throws SlickException {
        sprite = new Image("data/graphics/world objects/Sign.gif");
    }

    public void render(int camX, int camY) {
        sprite.draw(x + camX, y + camY);
    }
}

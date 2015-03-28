package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Spikes {

    public static final int DAMAGE = 5;
    private static Image sprite;
    public int x;
    public int y;

    public Spikes(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static void init() throws SlickException {
        sprite = new Image("data/graphics/world objects/Spikes.png");
    }

    public void render(int camX, int camY) {
        sprite.draw(x + camX, y + camY);
    }
}

package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Sign {
    int x;
    int y;
    String message;
    static Image sprite;
    
    Sign(int x, int y, String message) {
        this.x = x;
        this.y = y;
        this.message = message;
    }
    
    static void init() throws SlickException {
        sprite = new Image("data/graphics/sign.gif");
    }
    
    void render(int camX, int camY) {
        sprite.draw(x+camX, y+camY);
    }
}

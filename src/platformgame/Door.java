package platformgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Door {
    int x;
    int y;
    String path;
    static Image sprite;
    
    Door(int x, int y, String path) {
        this.x = x;
        this.y = y;
        this.path = path;
    }
    
    static void init() throws SlickException {
        sprite = new Image("data/graphics/door.gif");
    }
    
    void render(int camX, int camY) {
        sprite.draw(x+camX, y+camY);
    }
}

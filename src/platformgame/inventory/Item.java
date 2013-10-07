package platformgame.inventory;

import org.newdawn.slick.SlickException;

public interface Item {
    
    void init() throws SlickException;
    void renderIcon(int x, int y);
    void renderSprite(int x, int y, int dir);
}
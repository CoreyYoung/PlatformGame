package platformgame.inventory.items;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import platformgame.inventory.AmmoItem;

public class CopperArrow extends AmmoItem {
    
    @Override
    public void init() throws SlickException {
        icon = new Image("data/graphics/items/ammoItems/copperArrow.gif");
        sprite = new Image("data/graphics/items/ammoItems/copperArrow.gif");
        attack = 5;
    }
    
    @Override
    public void renderIcon(int x, int y) {
        icon.draw(x+8, y+8);
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        sprite.setRotation(dir);
        sprite.draw(x, y);
        sprite.setRotation(0);
    }
}

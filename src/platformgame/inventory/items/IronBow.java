package platformgame.inventory.items;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import platformgame.inventory.RangedItem;

public class IronBow extends RangedItem{
    
    @Override
    public void init() throws SlickException {
        icon = new Image("data/graphics/items/rangedItems/ironBow.gif");
        attack = 5;
        speed = 30;
    }
    
    @Override
    public void renderIcon(int x, int y) {
        icon.draw(x+8, y+8);
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        
    }
}

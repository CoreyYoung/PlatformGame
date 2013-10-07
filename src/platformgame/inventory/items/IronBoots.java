package platformgame.inventory.items;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import platformgame.inventory.BootsItem;

public class IronBoots extends BootsItem {
    Image icon;
    
    @Override
    public void init() throws SlickException {
        icon = new Image("data/graphics/items/bootsItems/ironBoots.gif");
        stability = 2;
    }
    
    @Override
    public void renderIcon(int x, int y) {
        icon.draw(x+8, y+8);
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        
    }
}

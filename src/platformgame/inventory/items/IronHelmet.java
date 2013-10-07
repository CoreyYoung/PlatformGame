package platformgame.inventory.items;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import platformgame.inventory.HelmetItem;

public class IronHelmet extends HelmetItem {
    static Image icon;
    
    @Override
    public void init() throws SlickException {
        icon = new Image("data/graphics/items/helmetItems/ironHelmet.gif");
        defense = 10;
    }
    
    @Override
    public void renderIcon(int x, int y) {
        icon.draw(x+8, y+8);
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        
    }
}

package platformgame.inventory.items;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import platformgame.inventory.HelmetItem;

public class CopperHelmet extends HelmetItem {
    static Image icon;
    
    @Override
    public void init() throws SlickException {
        icon = new Image("data/graphics/items/helmetItems/copperHelmet.gif");
        defense = 5;
    }
    
    @Override
    public void renderIcon(int x, int y) {
        icon.draw(x+8, y+8);
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        
    }
}
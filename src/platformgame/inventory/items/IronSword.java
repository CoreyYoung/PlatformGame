package platformgame.inventory.items;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import platformgame.inventory.MeleeItem;

public class IronSword extends MeleeItem {
    boolean automatic = false;
    static final int speed = 5;
    static Image icon, sprite;
    
    @Override
    public void init() throws SlickException {
        icon = new Image("data/graphics/items/meleeItems/ironSwordIcon.gif");
        sprite = new Image("data/graphics/items/meleeItems/ironSword.gif");
        width = sprite.getWidth();
        height = sprite.getHeight();
        attack = 5;
        knockback = 3;
    }
    
    @Override
    public void renderIcon(int x, int y) {
        icon.draw(x+8, y+8);
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        if (dir == 0) {
            sprite.draw(x+32, y+32-(height/2));
        } else {
            sprite.draw(x, y+32-(height/2), -width, height);
        }
    }
}

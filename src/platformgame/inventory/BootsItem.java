package platformgame.inventory;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class BootsItem extends Item {
    public int defense;
    
    public BootsItem(String name, String path, Image sprite, Image icon, int defense) throws SlickException {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.defense = defense;
    }
    
    @Override
    public void renderIcon(int x, int y) {
        icon.draw(x+8, y+8);
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        sprite.draw(x, y);
    }
}

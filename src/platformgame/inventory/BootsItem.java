package platformgame.inventory;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class BootsItem extends Item {
    public int defense;
    public int stability;
    public Image sprite;
    public Image icon;
    
    public void init(String name, String path, Image sprite, Image icon, int defense, int stability) throws SlickException {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.defense = defense;
        this.stability = stability;
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

package platformgame.inventory;

import org.newdawn.slick.Image;

public class AmmoItem extends Item {
    public int attack;
    
    public void init(String name, String path, Image sprite, Image icon, int attack) {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.attack = attack;
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        sprite.setRotation(dir);
        sprite.draw(x, y);
        sprite.setRotation(0);
    }
}

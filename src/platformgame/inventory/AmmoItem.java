package platformgame.inventory;

import org.newdawn.slick.Image;

public class AmmoItem extends Item {
    public int attack;
    public float knockback;
    
    public AmmoItem(String name, String path, Image sprite, Image icon, int attack, float knockback) {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.attack = attack;
        this.knockback = knockback;
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        sprite.setRotation(dir);
        sprite.draw(x, y);
        sprite.setRotation(0);
    }
}
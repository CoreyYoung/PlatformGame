package platformgame.inventory;

import org.newdawn.slick.Image;

public class MeleeItem extends Item {
    public int attack;
    public int knockback;
    public int speed;
    public int height;
    public int width;
    
    public void init(String name, String path, Image sprite, Image icon, int attack, int knockback, int speed) {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.attack = attack;
        this.knockback = knockback;
        this.speed = speed;
        
        width = sprite.getWidth();
        height = sprite.getHeight();
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
package platformgame.inventory;

import org.newdawn.slick.Image;

public class RangedItem extends Item {
    public int attack;
    public int speed;
    
    public RangedItem(String name, String path, Image sprite, Image icon, int attack, int speed) {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.attack = attack;
        this.speed = speed;
    }
    
    @Override
    public void renderSprite(int x, int y, int dir) {
        int width = sprite.getWidth();
        int height = sprite.getHeight();
        if (dir == 0) {
            sprite.draw(x+32, y+32-(height/2));
        } else {
            sprite.draw(x, y+32-(height/2), -width, height);
        }
    }
}
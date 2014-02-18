package platformgame.inventory;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class RangedItem extends Item {

    public int attack;
    public int speed;
    public Sound bowSound;

    public RangedItem(String name, String path, Image sprite, Image icon, int attack, int speed) throws SlickException {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.attack = attack;
        this.speed = speed;
        bowSound = new Sound("data/sounds/Bow.ogg");
    }

    @Override
    public void renderSprite(int x, int y, int dir) {
        int width = sprite.getWidth();
        int height = sprite.getHeight();
        if (dir == 0) {
            sprite.draw(x + 32, y + 32 - (height / 2));
        } else {
            sprite.draw(x, y + 32 - (height / 2), -width, height);
        }
    }
}

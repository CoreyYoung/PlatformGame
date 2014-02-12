package platformgame.inventory;

import org.newdawn.slick.Image;

public class RingItem extends Item {

    public int effect;

    public final static int NONE = -1;
    public final static int HEALING = 0;
    public final static int SPEED = 1;

    public RingItem(String name, String path, Image sprite, Image icon, int effect) {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.effect = effect;
    }
}

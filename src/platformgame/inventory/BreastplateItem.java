package platformgame.inventory;

import org.newdawn.slick.Image;

public class BreastplateItem extends Item {

    public int defense;

    public BreastplateItem(String name, String path, Image sprite, Image icon, int defense) {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.defense = defense;
    }
}

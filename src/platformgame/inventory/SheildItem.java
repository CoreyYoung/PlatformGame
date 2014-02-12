package platformgame.inventory;

import org.newdawn.slick.Image;

public class SheildItem extends Item {

    public int defense;
    public int stability;

    public SheildItem(String name, String path, Image sprite, Image icon, int defense, int stability) {
        this.name = name;
        this.path = path;
        this.sprite = sprite;
        this.icon = icon;
        this.defense = defense;
        this.stability = stability;
    }
}

package platformgame.inventory;

import org.newdawn.slick.Image;

public abstract class MeleeItem implements Item {
    boolean automatic = false;
    int speed = 1;
    public int knockback = 1;
    public int height;
    public int width;
    public int attack;
    public Image icon, sprite;
}
package platformgame.inventory;

import org.newdawn.slick.Image;

public abstract class Item {

    public String path = "Error: No path found.";
    public String name = "Error: No name found.";
    public Image icon;
    public Image sprite;

    public void renderIcon(int x, int y) {
        int width = icon.getWidth();
        int height = icon.getHeight();
        icon.draw(x + 24 - width / 2, y + 24 - height / 2);
    }

    public void renderSprite(int x, int y, int dir) {
        sprite.draw(x, y);
    }
}

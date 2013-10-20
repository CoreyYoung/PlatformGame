package platformgame.inventory;

import org.newdawn.slick.SlickException;

public class InventorySlot {
    public int amount;
    public Item item;
    
    InventorySlot (Item item, int amount) throws SlickException {
        this.item = item;
        this.amount = amount;
        this.item.init();
    }
    
    void render(int x, int y) {
        item.renderIcon(x, y);
    }
}

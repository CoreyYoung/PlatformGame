package platformgame.inventory;

import org.newdawn.slick.SlickException;

public class ItemStack {
    public int amount;
    public Item item;
    
    ItemStack (Item item, int amount) throws SlickException {
        this.item = item;
        this.amount = amount;
        this.item.init();
    }
    
    void render(int x, int y) {
        item.renderIcon(x, y);
    }
}

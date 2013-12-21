package platformgame.inventory;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ItemStack {
    public int amount;
    public Item item;
    
    public ItemStack (Item item, int amount) throws SlickException {
        this.item = item;
        this.amount = amount;
        //this.item.init();
    }
    
    void render(int x, int y, Graphics g) {
        item.renderIcon(x, y);
        if (amount > 1) {
            Font font = g.getFont();
            String string = Integer.toString(amount);
            g.drawString(string, x+42-font.getWidth(string), y+28);
        }
    }
}
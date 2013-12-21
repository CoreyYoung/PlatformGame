package platformgame;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import platformgame.inventory.Inventory;
import platformgame.inventory.ItemStack;

public class Chest {
    int x;
    int y;
    static Image sprite;
    static List<Chest> chestList = new ArrayList<>();
    ItemStack itemStack;
    
    public Chest(int x, int y, ItemStack itemStack) {
        this.x = x;
        this.y = y;
        this.itemStack = itemStack;
    }
    
    public static void init() throws SlickException {
        sprite = new Image("data/graphics/chest.gif");
    }
    
    public static void render(int camX, int camY) {
        for (Chest chest : chestList) {
            if (chest != null) {
                sprite.draw(chest.x+camX, chest.y+camY);
            }
        }
    }
    
    public static void createChest(int x, int y, ItemStack itemStack) {
        Chest chest = new Chest(x, y, itemStack);
        Chest.chestList.add(chest);
    }
    
    public void useChest() throws SlickException {
        if (! World.isChestOpened(this)) {
            String itemName = itemStack.item.name;

            if (itemStack.amount > 1) {
                Hud.addMessage("You found " + itemStack.amount + " " + itemName + "s! ");
            } else if (itemName.toLowerCase().charAt(0) == 'a'
                    || itemName.toLowerCase().charAt(0) == 'e'
                    || itemName.toLowerCase().charAt(0) == 'i'
                    || itemName.toLowerCase().charAt(0) == 'o'
                    || itemName.toLowerCase().charAt(0) == 'u') {
                Hud.addMessage("You found an " + itemName + "! ");
            } else {
                Hud.addMessage("You found a " + itemName + "! ");
            }
            Inventory.addItemStack(itemStack);
            World.setChestOpened(this);
        } else {
            Hud.addMessage("You didn't find anything. ");
        }
    }
}
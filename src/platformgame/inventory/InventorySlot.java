package platformgame.inventory;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class InventorySlot {
    public int x;
    public int y;
    public ItemStack itemStack;
    
    InventorySlot() {
        //Do nothing
    }
    
    public InventorySlot(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public InventorySlot(int x, int y, ItemStack itemStack) {
        this.x = x;
        this.y = y;
        this.itemStack = itemStack;
    }
    
    public void update() {
        if (itemStack != null) {
            if (itemStack.amount <= 0) {
                itemStack = null;
            }
        }
    }
    
    public void onLeftClick() {
        if (Inventory.mouseSlot == null || itemStack == null) {
            ItemStack tempStack = itemStack;
            itemStack = Inventory.mouseSlot;
            Inventory.mouseSlot = tempStack;
        } else {
            if (Inventory.mouseSlot.item.name.equals(itemStack.item.name)) {
                itemStack.amount += Inventory.mouseSlot.amount;
                Inventory.mouseSlot = null;
            } else {
                ItemStack tempStack = itemStack;
                itemStack = Inventory.mouseSlot;
                Inventory.mouseSlot = tempStack;
            }
        }
    }
    
    public void onRightClick() throws SlickException {
        if (EquipmentSlot.canAnyEquipItem(itemStack)) {
            for (EquipmentSlot equipmentSlot : Inventory.equipSlots) {
                if (equipmentSlot.canEquipItem(itemStack) && itemStack != null) {
                    ItemStack tempStack = equipmentSlot.itemStack;
                    equipmentSlot.itemStack = itemStack;
                    itemStack = tempStack;
                }
            }
        } else {
            if (Inventory.mouseSlot == null) {
                if (itemStack != null) {
                    if (itemStack.amount > 1) {
                        Inventory.mouseSlot = new ItemStack(itemStack.item, 1);
                        itemStack.amount --;
                    } else {
                        Inventory.mouseSlot = new ItemStack(itemStack.item, 1);
                        itemStack = null;
                    }
                }
            } else if (itemStack == null) {
                if (Inventory.mouseSlot.amount > 1) {
                    itemStack = new ItemStack(Inventory.mouseSlot.item, 1);
                    Inventory.mouseSlot.amount --;
                } else {
                    itemStack = new ItemStack(Inventory.mouseSlot.item, 1);
                    Inventory.mouseSlot = null;
                }
            } else if (Inventory.mouseSlot.item.name.equals(itemStack.item.name)) {
                if (itemStack.amount > 1) {
                    Inventory.mouseSlot.amount ++;
                    itemStack.amount --;
                } else {
                    itemStack = null;
                    Inventory.mouseSlot.amount ++;
                }
            }
        }
    }
    
    public void render(Graphics g, Input input) {
        if (itemStack != null) {
            itemStack.render(x, y, g);
            
            if (input.getMouseX() >= x && input.getMouseX() <= x + 48
                    && input.getMouseY() >= y && input.getMouseY() <= y + 48) {
                
                Font font = g.getFont();
                String itemName = itemStack.item.name;
                g.drawString(itemName, x + 24 - font.getWidth(itemName)/2, y);
            }
        }
    }
}
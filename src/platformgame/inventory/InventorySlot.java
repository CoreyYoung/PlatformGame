package platformgame.inventory;

import org.newdawn.slick.Graphics;

public class InventorySlot {
    public int x;
    public int y;
    public ItemStack itemStack;
    
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
        ItemStack tempStack = itemStack;
        itemStack = Inventory.mouseSlot;
        Inventory.mouseSlot = tempStack;
    }
    
    public void onRightClick(){
        for (EquipmentSlot equipmentSlot : Inventory.equipSlots) {
            if (equipmentSlot.canEquipItem(itemStack) && itemStack != null) {
                ItemStack tempStack = equipmentSlot.itemStack;
                equipmentSlot.itemStack = itemStack;
                itemStack = tempStack;
            }
        }
    }
    
    public void render(Graphics g) {
        if (itemStack != null) {
            itemStack.render(x, y, g);
        }
    }
}
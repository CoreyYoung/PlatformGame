package platformgame.inventory;

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
    
    public void render() {
        if (itemStack != null) {
            itemStack.render(x, y);
        }
    }
}
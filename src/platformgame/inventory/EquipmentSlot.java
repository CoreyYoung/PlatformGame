package platformgame.inventory;

public class EquipmentSlot {
    public int x;
    public int y;
    public ItemStack itemStack;
    public int itemType;
    
    public static final int RING_SLOT = 0;
    public static final int HELMET_SLOT = 1;
    public static final int SHEILD_SLOT = 2;
    public static final int CHESTPLATE_SLOT = 3;
    public static final int RANGED_SLOT = 4;
    public static final int LEGGINGS_SLOT = 5;
    public static final int MELEE_SLOT = 6;
    public static final int BOOTS_SLOT = 7;
    
    public EquipmentSlot(int x, int y, ItemStack itemStack, int itemType) {
        this.x = x;
        this.y = y;
        this.itemStack = itemStack;
        this.itemType = itemType;
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
    
    public boolean canEquipItem(ItemStack itemStack) {
        if (itemStack == null) {
            return true;
        }
        
        return ((itemType == RING_SLOT && itemStack.item instanceof HandItem)
                || (itemType == HELMET_SLOT && itemStack.item instanceof HelmetItem)
                || (itemType == SHEILD_SLOT && itemStack.item instanceof SheildItem)
                || (itemType == CHESTPLATE_SLOT && itemStack.item instanceof ChestPlateItem)
                || (itemType == RANGED_SLOT && itemStack.item instanceof RangedItem)
                || (itemType == LEGGINGS_SLOT && itemStack.item instanceof LeggingsItem)
                || (itemType == MELEE_SLOT && itemStack.item instanceof MeleeItem)
                || (itemType == BOOTS_SLOT && itemStack.item instanceof BootsItem));
    }
}
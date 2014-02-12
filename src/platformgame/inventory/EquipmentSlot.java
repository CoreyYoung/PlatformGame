package platformgame.inventory;

public class EquipmentSlot extends InventorySlot {

    public int itemType;

    public static final int RING_SLOT = 0;
    public static final int HELMET_SLOT = 1;
    public static final int SHEILD_SLOT = 2;
    public static final int BREASTPLATE_SLOT = 3;
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

    @Override
    public void onLeftClick() {
        if (canEquipItem(Inventory.mouseSlot)) {
            ItemStack tempStack = Inventory.mouseSlot;
            Inventory.mouseSlot = itemStack;
            itemStack = tempStack;
        }
    }

    @Override
    public void onRightClick() {
        if (itemStack != null) {
            InventorySlot inventorySlot = Inventory.getFirstSlot(null);
            if (inventorySlot != null) {
                inventorySlot.itemStack = itemStack;
                itemStack = null;
            }
        }
    }

    public boolean canEquipItem(ItemStack itemStack) {
        if (itemStack == null) {
            return true;
        }

        return ((itemType == RING_SLOT && itemStack.item instanceof RingItem)
                || (itemType == HELMET_SLOT && itemStack.item instanceof HelmetItem)
                || (itemType == SHEILD_SLOT && itemStack.item instanceof SheildItem)
                || (itemType == BREASTPLATE_SLOT && itemStack.item instanceof BreastplateItem)
                || (itemType == RANGED_SLOT && itemStack.item instanceof RangedItem)
                || (itemType == LEGGINGS_SLOT && itemStack.item instanceof LeggingsItem)
                || (itemType == MELEE_SLOT && itemStack.item instanceof MeleeItem)
                || (itemType == BOOTS_SLOT && itemStack.item instanceof BootsItem));
    }

    static public boolean canAnyEquipItem(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        return (itemStack.item instanceof RingItem
                || itemStack.item instanceof HelmetItem
                || itemStack.item instanceof SheildItem
                || itemStack.item instanceof BreastplateItem
                || itemStack.item instanceof RangedItem
                || itemStack.item instanceof LeggingsItem
                || itemStack.item instanceof MeleeItem
                || itemStack.item instanceof BootsItem);
    }
}

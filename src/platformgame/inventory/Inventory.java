package platformgame.inventory;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

abstract public class Inventory {
    static Image guiImage;
    
    public static ItemStack mouseSlot;
    public static InventorySlot[][] invSlots = new InventorySlot[9][4];
    public static EquipmentSlot[] equipSlots = new EquipmentSlot[8];
    
    public static void init() throws SlickException {
        guiImage = new Image("data/graphics/GUI/inventory.png");
        
        createInventorySlots();
        createEquipmentSlots();
    }
    
    public static void update() {
        for (InventorySlot[] slotArray : invSlots) {
            for (InventorySlot slot : slotArray) {
                slot.update();
            }
        }
        
        for (EquipmentSlot slot : equipSlots) {
            slot.update();
        }
    }
    
    private static void createInventorySlots() throws SlickException {
        int xOffset = 48;
        int yOffset = 128+16;
        for (int i = 0; i < invSlots.length; i++) {
            for (int ii = 0; ii < invSlots[i].length; ii++) {
                invSlots[i][ii] = new InventorySlot(i*48+xOffset, ii*48+yOffset);
            }
        }
    }
    
    private static void createEquipmentSlots() throws SlickException {
        int xOffset = 48*10+24;
        int yOffset = 128+16;
        for (int i = 0; i < equipSlots.length; i ++) {
            int tempX, tempY;
            
            //if (i is an even number)
            if (i % 2 == 0) {
                tempX = 0;
                tempY = (i*48)/2;
            } else {
                tempX = 48;
                tempY = ((i-1)*48)/2;
            }
            equipSlots[i] = new EquipmentSlot(xOffset+tempX, yOffset+tempY, null, i);
        }
    }
    
    public static void useInventory(Input input) throws SlickException {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        Boolean leftPressed = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
        Boolean rightPressed = input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);

        for (InventorySlot[] slotArray : invSlots) {
            for (InventorySlot slot : slotArray) {
                if (mouseX >= slot.x && mouseX <= slot.x+48
                        && mouseY >= slot.y && mouseY <= slot.y+48) {
                    if (leftPressed) {
                        slot.onLeftClick();
                    } else if (rightPressed) {
                        slot.onRightClick();
                    }
                }
            }
        }

        for (EquipmentSlot slot : equipSlots) {
            if (mouseX >= slot.x && mouseX <=slot.x+48
                    && mouseY >= slot.y && mouseY <= slot.y+48) {
                if (leftPressed) {
                    slot.onLeftClick();
                } else if (rightPressed) {
                    slot.onRightClick();
                }
            }
        }
    }
    
    public static void render(Input input, Graphics g) {
        guiImage.draw();
        
        for (InventorySlot[] slotArray : invSlots) {
            for (InventorySlot slot : slotArray) {
                slot.render(g, input);
            }
        }
        
        for (EquipmentSlot slot : equipSlots) {
            slot.render(g, input);
        }
        
        if (mouseSlot != null) {
            mouseSlot.render(input.getMouseX(), input.getMouseY(), g);
        }
    }
    
    public static void addItemStack(ItemStack stack) throws SlickException {
        InventorySlot slot = getFirstSlot(stack.item);

        if (slot != null) {
            slot.itemStack.amount += stack.amount;
        } else {
            slot = getFirstSlot(null);
            if (slot != null) {
                slot.itemStack = new ItemStack(stack.item, stack.amount);
            } else {
                //Inventory is full!
            }
        }
    }
    
    public static int getDefense() {
        int defense = 0;
        
        EquipmentSlot bootsSlot = equipSlots[EquipmentSlot.BOOTS_SLOT];
        if (bootsSlot.itemStack != null) {
            BootsItem bootsItem = (BootsItem) bootsSlot.itemStack.item;
            defense += bootsItem.defense;
        }
        
        EquipmentSlot breastplateSlot = equipSlots[EquipmentSlot.BREASTPLATE_SLOT];
        if (breastplateSlot.itemStack != null) {
            BreastplateItem breastplateItem = (BreastplateItem) breastplateSlot.itemStack.item;
            defense += breastplateItem.defense;
        }
        
        EquipmentSlot helmetSlot = equipSlots[EquipmentSlot.HELMET_SLOT];
        if (helmetSlot.itemStack != null) {
            HelmetItem helmetItem = (HelmetItem) helmetSlot.itemStack.item;
            defense += helmetItem.defense;
        }
        
        EquipmentSlot leggingsSlot = equipSlots[EquipmentSlot.LEGGINGS_SLOT];
        if (leggingsSlot.itemStack != null) {
            LeggingsItem leggingsItem = (LeggingsItem) leggingsSlot.itemStack.item;
            defense += leggingsItem.defense;
        }
        
        EquipmentSlot sheildSlot = equipSlots[EquipmentSlot.SHEILD_SLOT];
        if (sheildSlot.itemStack != null) {
            SheildItem sheildItem = (SheildItem) sheildSlot.itemStack.item;
            defense += sheildItem.defense;
        }
        
        return defense;
    }
    
    public static int getAttack() {
        EquipmentSlot meleeSlot = equipSlots[EquipmentSlot.MELEE_SLOT];
        if (meleeSlot.itemStack != null) {
            MeleeItem tempItem = (MeleeItem) meleeSlot.itemStack.item;
            return tempItem.attack;
        }
        
        return 1;
    }
    
    public static int getStability() {
        EquipmentSlot sheildSlot = equipSlots[EquipmentSlot.SHEILD_SLOT];
        if (sheildSlot.itemStack != null) {
            SheildItem sheildItem = (SheildItem) sheildSlot.itemStack.item;
            return sheildItem.stability;
        }
        
        return 0;
    }
    
    public static MeleeItem getMeleeItem() {
        EquipmentSlot meleeSlot = equipSlots[EquipmentSlot.MELEE_SLOT];
        if (meleeSlot.itemStack != null) {
            return (MeleeItem) meleeSlot.itemStack.item;
        }
        
        return null;
    }
    
    public static RangedItem getRangedItem() {
        EquipmentSlot rangedSlot = equipSlots[EquipmentSlot.RANGED_SLOT];
        if (rangedSlot.itemStack != null) {
            return (RangedItem) rangedSlot.itemStack.item;
        }
        
        return null;
    }
    
    public static AmmoItem getAmmoItem() {
        for (InventorySlot[] slotArray : invSlots) {
            for (InventorySlot slot : slotArray) {
                if (slot.itemStack != null) {
                    if (slot.itemStack.item instanceof AmmoItem) {
                        return (AmmoItem) slot.itemStack.item;
                    }
                }
            }
        }
        
        return null;
    }
    
    public static InventorySlot getFirstSlot(Item item) {
        for (InventorySlot[] slotArray : invSlots) {
            for (InventorySlot slot : slotArray) {
                if (slot.itemStack == null) {
                    if (item == null) {
                        return slot;
                    }
                } else if (item != null) {
                    if (slot.itemStack.item.name.equals(item.name)) {
                        return slot;
                    }
                }
            }
        }
        
        return null;
    }
}
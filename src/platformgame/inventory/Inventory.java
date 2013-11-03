package platformgame.inventory;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import platformgame.inventory.items.*;

abstract public class Inventory {
    static Image guiImage;
    
    public static ItemStack mouseSlot;
    static InventorySlot[][] invSlots = new InventorySlot[9][4];
    public static EquipmentSlot[] equipSlots = new EquipmentSlot[8];
    
    public static void init() throws SlickException {
        guiImage = new Image("data/graphics/GUI/inventory.gif");
        
        createInventorySlots();
        createEquipmentSlots();
        
        invSlots[0][0].itemStack = new ItemStack(new IronSword(), 5);
        invSlots[1][0].itemStack = new ItemStack(new IronHelmet(), 1);
        invSlots[2][0].itemStack = new ItemStack(new IronBoots(), 1);
        invSlots[3][0].itemStack = new ItemStack(new CopperHelmet(), 1);
        invSlots[4][0].itemStack = new ItemStack(new CopperArrow(), 250);
        invSlots[5][0].itemStack = new ItemStack(new IronBow(), 1);
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
                ItemStack stack = new ItemStack(new CopperHelmet(), 1);
                invSlots[i][ii] = new InventorySlot(i*48+xOffset, ii*48+yOffset, stack);
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
    
    public static void useInventory(Input input) {
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
                slot.render(g);
            }
        }
        
        for (EquipmentSlot slot : equipSlots) {
            slot.render(g);
        }
        
        if (mouseSlot != null) {
            mouseSlot.render(input.getMouseX(), input.getMouseY(), g);
        }
    }
    
    public static int getDefense() {
        int defense = 0;
        EquipmentSlot helmetSlot = equipSlots[EquipmentSlot.HELMET_SLOT];
        if (helmetSlot.itemStack != null) {
            HelmetItem tempItem = (HelmetItem) helmetSlot.itemStack.item;
            defense += tempItem.defense;
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
        EquipmentSlot bootsSlot = equipSlots[EquipmentSlot.BOOTS_SLOT];
        if (bootsSlot.itemStack != null) {
            BootsItem tempItem = (BootsItem) bootsSlot.itemStack.item;
            return tempItem.stability;
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
                } else if (slot.itemStack.item == item) {
                    return slot;
                }
            }
        }
        return null;
    }
}
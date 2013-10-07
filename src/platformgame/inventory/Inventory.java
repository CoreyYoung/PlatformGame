package platformgame.inventory;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import platformgame.inventory.items.*;

abstract public class Inventory {
    static final int xOffset = 24;
    static final int yOffset = 48;
    static final int slotSpacing = 0;
    static final int rows = 7;
    static final int columns = 8;
    static Image guiImage;
    static int slotWidth = 48;
    static int slotHeight = 48;
    static InventorySlot[][] inventorySlot = new InventorySlot[rows][columns];
    public static InventorySlot mouseSlot, helmetSlot, rangedSlot, amuletSlot, ammoSlot,
            rightHandSlot, chestPlateSlot, leftHandSlot, meleeSlot, leggingsSlot, sheildSlot,
            bootsSlot;
    
    public static void init() throws SlickException {
        guiImage = new Image("data/graphics/GUI/inventory.gif");
        inventorySlot[0][0] = new InventorySlot(new IronSword(), 1);
        inventorySlot[1][0] = new InventorySlot(new IronHelmet(), 1);
        inventorySlot[2][0] = new InventorySlot(new IronBoots(), 1);
        inventorySlot[3][0] = new InventorySlot(new CopperHelmet(), 1);
    }
    
    public static void update(Input input) {
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            int mouseX = input.getMouseX();
            int mouseY = input.getMouseY();
            
            int xMax = xOffset+rows*(slotWidth+slotSpacing);
            int yMax = yOffset+columns*(slotHeight+slotSpacing);
            
            if (mouseX >= xOffset && mouseX <= xMax
                    && mouseY >= yOffset && mouseY <= yMax) {
                int slotX = (mouseX-xOffset)/(slotWidth+slotSpacing);
                int slotY = (mouseY-yOffset)/(slotHeight+slotSpacing);
                InventorySlot tempSlot = inventorySlot[slotX][slotY];
                inventorySlot[slotX][slotY] = mouseSlot;
                mouseSlot = tempSlot;
                System.out.println("Slot:"+slotX+","+slotY);
            } else if (mouseX >= (8*slotWidth)+24 && mouseX <= (9*slotWidth)+24) {
                if (mouseY >= (slotHeight*3) && mouseY <= (slotHeight*4)) {
                    useEquipmentSlot("rangedSlot");
                } else if (mouseY >= (slotHeight*4)+24 && mouseY <= (slotHeight*5)+24) {
                    useEquipmentSlot("leftHandSlot");
                } else if (mouseY >= (slotHeight*5)+24*2 && mouseY <= (slotHeight*6)+24*2) {
                    useEquipmentSlot("meleeSlot");
                }
            } else if (mouseX >= 10*slotWidth && mouseX <= 11*slotWidth) {
                if (mouseY >= (slotHeight)+24 && mouseY <= (2*slotHeight)+24) {
                    useEquipmentSlot("helmetSlot");
                } else if (mouseY >= (2*slotHeight)+24*2 && mouseY <= (3*slotHeight)+24*2) {
                    useEquipmentSlot("amuletSlot");
                } else if (mouseY >= (3*slotHeight)+24*3 && mouseY <= (4*slotHeight)+24*3) {
                    useEquipmentSlot("chestPlateSlot");
                } else if (mouseY >= (4*slotHeight)+24*4 && mouseY <= (5*slotHeight)+24*4) {
                    useEquipmentSlot("leggingsSlot");
                } else if (mouseY >= (5*slotHeight)+24*5 && mouseY <= (6*slotHeight)+24*5) {
                    useEquipmentSlot("bootsSlot");
                }
            } else if (mouseX >= (11*slotWidth)+24 && mouseX <= (12*slotWidth)+24) {
                if (mouseY >= (slotHeight*3) && mouseY <= (slotHeight*4)) {
                    useEquipmentSlot("ammoSlot");
                } else if (mouseY >= (slotHeight*4)+24 && mouseY <= (slotHeight*5)+24) {
                    useEquipmentSlot("rightHandSlot");
                } else if (mouseY >= (slotHeight*5)+24*2 && mouseY <= (slotHeight*6)+24*2) {
                    useEquipmentSlot("sheildSlot");
                }
            }
            System.out.println("MousePos:"+mouseX+","+mouseX);
        }
    }
    
    public static void render(Input input) {
        guiImage.draw();
        
        if (mouseSlot != null) {
            mouseSlot.item.renderIcon(input.getMouseX(), input.getMouseY());
        }
        
        for (int i = 0; i < rows; i ++) {
            for (int ii = 0; ii < columns; ii ++) {
                int slotX = xOffset+i*(slotWidth+slotSpacing);
                int slotY = yOffset+ii*(slotHeight+slotSpacing);
                
                if (inventorySlot[i][ii] != null) {
                    inventorySlot[i][ii].item.renderIcon(slotX, slotY);
                }
            }
        }
        renderEquipmentSlots();
    }
    
    static void useEquipmentSlot(String invSlot) {
        if (invSlot.equals("rangedSlot") && (mouseSlot == null || mouseSlot.item instanceof RangedItem )) {
            InventorySlot tempSlot = rangedSlot;
            rangedSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("leftHandSlot") && (mouseSlot == null || mouseSlot.item instanceof HandItem )) {
            InventorySlot tempSlot = leftHandSlot;
            leftHandSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("meleeSlot") && (mouseSlot == null || mouseSlot.item instanceof MeleeItem )) {
            InventorySlot tempSlot = meleeSlot;
            meleeSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("helmetSlot") && (mouseSlot == null || mouseSlot.item instanceof HelmetItem )) {
            InventorySlot tempSlot = helmetSlot;
            helmetSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("amuletSlot") && (mouseSlot == null || mouseSlot.item instanceof AmuletItem )) {
            InventorySlot tempSlot = amuletSlot;
            amuletSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("chestPlateSlot") && (mouseSlot == null || mouseSlot.item instanceof ChestPlateItem )) {
            InventorySlot tempSlot = chestPlateSlot;
            chestPlateSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("leggingsSlot") && (mouseSlot == null || mouseSlot.item instanceof LeggingsItem )) {
            InventorySlot tempSlot = leggingsSlot;
            leggingsSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("bootsSlot") && (mouseSlot == null || mouseSlot.item instanceof BootsItem )) {
            InventorySlot tempSlot = bootsSlot;
            bootsSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("ammoSlot") && (mouseSlot == null || mouseSlot.item instanceof AmmoItem )) {
            InventorySlot tempSlot = ammoSlot;
            ammoSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("rightHandSlot") && (mouseSlot == null || mouseSlot.item instanceof HandItem )) {
            InventorySlot tempSlot = rightHandSlot;
            rightHandSlot = mouseSlot;
            mouseSlot = tempSlot;
        } else if (invSlot.equals("sheildSlot") && (mouseSlot == null || mouseSlot.item instanceof SheildItem )) {
            InventorySlot tempSlot = sheildSlot;
            sheildSlot = mouseSlot;
            mouseSlot = tempSlot;
        }
    }
    
    static void renderEquipmentSlots() {
        renderSlot(rangedSlot,      8*slotWidth+24*1, 2*slotHeight+24*2);
        renderSlot(leftHandSlot,    8*slotWidth+24*1, 3*slotHeight+24*3);
        renderSlot(meleeSlot,       8*slotWidth+24*1, 4*slotHeight+24*4);
        
        renderSlot(helmetSlot,      9*slotWidth+24*2, 1*slotHeight+24*1);
        renderSlot(amuletSlot,      9*slotWidth+24*2, 2*slotHeight+24*2);
        renderSlot(chestPlateSlot,  9*slotWidth+24*2, 3*slotHeight+24*3);
        renderSlot(leggingsSlot,    9*slotWidth+24*2, 4*slotHeight+24*4);
        renderSlot(bootsSlot,       9*slotWidth+24*2, 5*slotHeight+24*5);
        
        renderSlot(ammoSlot,       10*slotWidth+24*3, 2*slotHeight+24*2);
        renderSlot(rightHandSlot,  10*slotWidth+24*3, 3*slotHeight+24*3);
        renderSlot(sheildSlot,     10*slotWidth+24*3, 4*slotHeight+24*4);
    }
    
    static void renderSlot(InventorySlot invSlot, int x, int y) {
        if (invSlot != null) {
            invSlot.item.renderIcon(x, y);
        }
    }
    
    public static int getDefense() {
        int defense = 0;
        if (helmetSlot != null) {
            HelmetItem tempItem = (HelmetItem) helmetSlot.item;
            defense += tempItem.defense;
        }
        return defense;
    }
    
    public static int getAttack() {
        if (meleeSlot != null) {
            MeleeItem tempItem = (MeleeItem) meleeSlot.item;
            return tempItem.attack;
        }
        return 1;
    }
    
    public static int getStability() {
        if (bootsSlot != null) {
            BootsItem tempItem = (BootsItem) bootsSlot.item;
            return tempItem.stability;
        }
        return 0;
    }
    
    public static MeleeItem getMeleeItem() {
        if (meleeSlot != null) {
            return (MeleeItem) meleeSlot.item;
        }
        return null;
    }
}
package platformgame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.newdawn.slick.SlickException;
import platformgame.inventory.EquipmentSlot;
import platformgame.inventory.Inventory;
import platformgame.inventory.InventorySlot;
import platformgame.inventory.Item;
import platformgame.inventory.ItemStack;

public class GameSave {
    private static final String fileName = "data/save.txt";
    
    public static void loadGame() throws SlickException {
        try {
            FileReader fileReader = new FileReader(fileName);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                loadPlayerData(bufferedReader);
                loadWorldData(bufferedReader);
                
                loadInventorySlotsData(bufferedReader);
                loadEquipmentSlotsData(bufferedReader);
                
                loadChestData(bufferedReader);
            }
        }
        catch (FileNotFoundException e) {
            System.err.println(e);
        }
        catch (IOException e) {
            System.err.println(e);
        }
    }
    
    private static void loadPlayerData(BufferedReader bufferedReader) throws IOException {
        Player.x = Integer.parseInt(bufferedReader.readLine());
        Player.y = Integer.parseInt(bufferedReader.readLine());
    }
    
    private static void loadWorldData(BufferedReader bufferedReader) throws IOException, SlickException {
        World.init(bufferedReader.readLine());
    }
    
    private static void loadInventorySlotsData(BufferedReader bufferedReader) throws IOException, SlickException {
        for (InventorySlot[] slotArray : Inventory.invSlots) {
            for (InventorySlot slot : slotArray) {
                slot.itemStack = loadItemStack(bufferedReader);
            }
        }
    }
    
    private static void loadEquipmentSlotsData(BufferedReader bufferedReader) throws IOException, SlickException {
        for (EquipmentSlot slot : Inventory.equipSlots) {
            slot.itemStack = loadItemStack(bufferedReader);
        }
    }
    
    private static ItemStack loadItemStack(BufferedReader bufferedReader) throws IOException, SlickException {
        ItemStack itemStack = null;
        String itemName = bufferedReader.readLine();
        if (! itemName.isEmpty()) {
            try {
                Class tempClass = Class.forName(itemName);
                int amount = Integer.parseInt(bufferedReader.readLine());
                Item item = (Item) tempClass.newInstance();
                itemStack =  new ItemStack(item, amount); 
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                System.err.println(ex);
            }
        } else {
            itemStack = null;
        }
        
        return itemStack;
    }
    
    private static void loadChestData(BufferedReader bufferedReader) throws IOException {
        int chestNum = Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < chestNum; i ++) {
            String chestID = bufferedReader.readLine();
            if (! World.openedChestList.contains(chestID)) {
                World.openedChestList.add(chestID);
            }
        }
    }
    
    public static void saveGame() {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                savePlayerData(bufferedWriter);
                saveWorldData(bufferedWriter);
                
                saveInventorySlotsData(bufferedWriter);
                saveEquipmentSlotsData(bufferedWriter);
                
                saveChestData(bufferedWriter);
            }
        }
        catch (IOException e) {
            System.err.println(e);
        }
    }
    
    private static void savePlayerData(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(Integer.toString((int) Player.x));
        bufferedWriter.newLine();
        bufferedWriter.write(Integer.toString((int) Player.y));
        bufferedWriter.newLine();
    }
    
    private static void saveWorldData(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(World.levelName);
        bufferedWriter.newLine();
    }
    
    private static void saveInventorySlotsData(BufferedWriter bufferedWriter) throws IOException {
        for (InventorySlot[] slotArray : Inventory.invSlots) {
            for (InventorySlot slot : slotArray) {
                if (slot.itemStack != null) {
                    bufferedWriter.write(slot.itemStack.item.getClass().getCanonicalName());
                    bufferedWriter.newLine();
                    bufferedWriter.write(Integer.toString(slot.itemStack.amount));
                }
                bufferedWriter.newLine();
            }
        }
    }
    
    private static void saveEquipmentSlotsData(BufferedWriter bufferedWriter) throws IOException {
        for (EquipmentSlot slot : Inventory.equipSlots) {
            if (slot.itemStack != null) {
                bufferedWriter.write(slot.itemStack.item.getClass().getCanonicalName());
                bufferedWriter.newLine();
                bufferedWriter.write(Integer.toString(slot.itemStack.amount));
            }
            bufferedWriter.newLine();
        }
    }
    
    private static void saveChestData(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(Integer.toString(World.openedChestList.size()));
        bufferedWriter.newLine();
        
        for (String chestString : World.openedChestList) {
            bufferedWriter.write(chestString);
            bufferedWriter.newLine();
        }
    }
}
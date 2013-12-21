package platformgame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import platformgame.inventory.AmmoItem;
import platformgame.inventory.BootsItem;
import platformgame.inventory.EquipmentSlot;
import platformgame.inventory.HelmetItem;
import platformgame.inventory.Inventory;
import platformgame.inventory.InventorySlot;
import platformgame.inventory.Item;
import platformgame.inventory.ItemStack;
import platformgame.inventory.MeleeItem;
import platformgame.inventory.RangedItem;

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
                
                //TESTING! REMOVE AFTERWARDS!!!!!
                //Item item = loadItem("data/items/IronBoots.txt");
                //ItemStack itemStack = new ItemStack(item, 1);
                //Inventory.addItemStack(itemStack);
                
                bufferedReader.close();
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
        String itemName = bufferedReader.readLine();
        
        if ((! itemName.isEmpty())) {
            int amount = Integer.parseInt(bufferedReader.readLine());
            
            if (! itemName.equals("Error: No path found.")) {
                Item item = loadItem(itemName);
                return new ItemStack(item, amount); 
            }
        }
        
        return null;
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
                
                bufferedWriter.close();
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
                saveItemStack(bufferedWriter, slot.itemStack);
            }
        }
    }
    
    private static void saveEquipmentSlotsData(BufferedWriter bufferedWriter) throws IOException {
        for (EquipmentSlot slot : Inventory.equipSlots) {
            saveItemStack(bufferedWriter, slot.itemStack);
        }
    }
    
    private static void saveItemStack(BufferedWriter bufferedWriter, ItemStack itemStack) throws IOException {
        if (itemStack != null) {
            bufferedWriter.write(itemStack.item.path);
            bufferedWriter.newLine();
            bufferedWriter.write(Integer.toString(itemStack.amount));
        }
        
        bufferedWriter.newLine();
    }
    
    private static void saveChestData(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(Integer.toString(World.openedChestList.size()));
        bufferedWriter.newLine();
        
        for (String chestString : World.openedChestList) {
            bufferedWriter.write(chestString);
            bufferedWriter.newLine();
        }
    }
    
    public static Item loadItem(String path) throws SlickException {
        Item item = null;
        
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            try {
                String itemName = bufferedReader.readLine();
                String itemType = bufferedReader.readLine();
                
                Image icon = new Image(bufferedReader.readLine());
                Image sprite = new Image(bufferedReader.readLine());
                
                switch (itemType) {
                    case "AmmoItem": {
                        int attack = Integer.parseInt(bufferedReader.readLine());
                        
                        AmmoItem ammoItem = new AmmoItem();
                        ammoItem.init(itemName, path, sprite, icon, attack);
                        
                        item = ammoItem;
                        break;
                    }
                        
                    case "BootsItem": {
                        int defense = Integer.parseInt(bufferedReader.readLine());
                        int stability = Integer.parseInt(bufferedReader.readLine());
                        
                        BootsItem bootsItem = new BootsItem();
                        bootsItem.init(itemName, path, sprite, icon, defense, stability);
                        
                        item = bootsItem;
                        break;
                    }
                    
                    case "HelmetItem": {
                        int defense = Integer.parseInt(bufferedReader.readLine());
                        
                        HelmetItem helmetItem = new HelmetItem();
                        helmetItem.init(itemName, path, sprite, icon, defense);
                        
                        item = helmetItem;
                        break;
                    }
                    
                    case "MeleeItem": {
                        int attack = Integer.parseInt(bufferedReader.readLine());
                        int knockback = Integer.parseInt(bufferedReader.readLine());
                        int speed = Integer.parseInt(bufferedReader.readLine());
                        
                        MeleeItem meleeItem = new MeleeItem();
                        meleeItem.init(path, path, sprite, icon, attack, knockback, speed);
                        
                        item = meleeItem;
                        break;
                    }
                        
                    case "RangedItem": {
                        int attack = Integer.parseInt(bufferedReader.readLine());
                        int speed = Integer.parseInt(bufferedReader.readLine());
                        
                        RangedItem rangedItem = new RangedItem();
                        rangedItem.init(itemName, path, sprite, icon, attack, speed);
                        
                        item = rangedItem;
                        break;
                    }
                }
                
            } catch (IOException ex) {
                Logger.getLogger(GameSave.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameSave.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return item;
    }
}
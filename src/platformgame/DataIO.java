package platformgame;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.yaml.snakeyaml.Yaml;
import platformgame.inventory.*;

public class DataIO {

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

                bufferedReader.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void loadPlayerData(BufferedReader bufferedReader) throws IOException {
        Player.x = readInt(bufferedReader);
        Player.y = readInt(bufferedReader);
        Player.health = Player.healthMAX;
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

        if ((!itemName.isEmpty())) {
            int amount = readInt(bufferedReader);

            if (!itemName.equals("Error: No path found.")) {
                Item item = loadItem(itemName);
                return new ItemStack(item, amount);
            }
        }

        return null;
    }

    private static void loadChestData(BufferedReader bufferedReader) throws IOException {
        int chestNum = readInt(bufferedReader);
        for (int i = 0; i < chestNum; i++) {
            String chestID = bufferedReader.readLine();
            if (!World.openedChestList.contains(chestID)) {
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
        } catch (IOException e) {
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
        try {
            FileReader fileReader = new FileReader(path);
            Yaml yaml = new Yaml();
            
            HashMap<String, Object> fileMap = (HashMap<String, Object>) yaml.load(fileReader);

            String name = (String) fileMap.get("Name");
            String type = (String) fileMap.get("Type");
            
            Image sprite = new Image((String) fileMap.get("Sprite"));
            Image icon = new Image((String) fileMap.get("Icon"));

            switch (type) {
                case "AmmoItem": {
                    int damage = (int) fileMap.get("Damage");
                    float knockback = (float) ((double) fileMap.get("Knockback"));
                    
                    return new AmmoItem(name, path, sprite, icon, damage, knockback);
                }

                case "BootsItem": {
                    int defense = (int) fileMap.get("Defense");

                    return new BootsItem(name, path, sprite, icon, defense);
                }

                case "BreastplateItem": {
                    int defense = (int) fileMap.get("Defense");

                    return new BreastplateItem(name, path, sprite, icon, defense);
                }

                case "HelmetItem": {
                    int defense = (int) fileMap.get("Defense");

                    return new HelmetItem(name, path, sprite, icon, defense);
                }

                case "LeggingsItem": {
                    int defense = (int) fileMap.get("Defense");

                    return new LeggingsItem(name, path, sprite, icon, defense);
                }

                case "MeleeItem": {
                    int attack = (int) fileMap.get("Attack");
                    float knockback = (float) ((double) fileMap.get("Knockback"));
                    int speed = (int) fileMap.get("Speed");

                    return new MeleeItem(name, path, sprite, icon, attack, knockback, speed);
                }

                case "RangedItem": {
                    int attack = (int) fileMap.get("Attack");
                    int speed = (int) fileMap.get("Speed");

                    return new RangedItem(name, path, sprite, icon, attack, speed);
                }

                case "SheildItem": {
                    int defense = (int) fileMap.get("Defense");
                    int stability = (int) fileMap.get("Stability");

                    return new SheildItem(name, path, sprite, icon, defense, stability);
                }

                default: {
                    System.err.println("Error: Item file contains invalid item type.");
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public static ZombieAI loadEnemy(String path) throws SlickException {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String type = bufferedReader.readLine();
            Image sprite = new Image(bufferedReader.readLine());
            
            
            switch (type) {
                case "ZombieAI": {
                    int healthMAX = readInt(bufferedReader);
                    int speedMAX = readInt(bufferedReader);
                    int JUMP_SPEED = readInt(bufferedReader);
                    float ACCELERATION = readFloat(bufferedReader);
                    int defense = readInt(bufferedReader);
                    int stability = readInt(bufferedReader);
                    int damage = readInt(bufferedReader);
                    int knockback = readInt(bufferedReader);
                    
                    return new ZombieAI(sprite, 0, 0, false, healthMAX, speedMAX,
                            JUMP_SPEED, ACCELERATION, defense, stability, damage, knockback);
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    private static int readInt(BufferedReader bufferedReader) throws IOException {
        return Integer.parseInt(bufferedReader.readLine());
    }
    
    private static float readFloat(BufferedReader bufferedReader) throws IOException {
        return Float.parseFloat(bufferedReader.readLine());
    }
}
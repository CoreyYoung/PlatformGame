package platformgame;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.yaml.snakeyaml.Yaml;
import platformgame.enemies.Enemy;
import platformgame.enemies.FlyingAI;
import platformgame.enemies.ZombieAI;
import platformgame.inventory.AmmoItem;
import platformgame.inventory.BootsItem;
import platformgame.inventory.BreastplateItem;
import platformgame.inventory.HelmetItem;
import platformgame.inventory.Inventory;
import platformgame.inventory.Item;
import platformgame.inventory.ItemStack;
import platformgame.inventory.LeggingsItem;
import platformgame.inventory.MeleeItem;
import platformgame.inventory.RangedItem;
import platformgame.inventory.RingItem;
import platformgame.inventory.SheildItem;

public class DataIO {

    private static final String fileName = "data/Save.yml";

    public static void loadGame() throws SlickException {
        HashMap<String, Object> fileMap = loadHashMap(fileName);

        loadPlayerData(fileMap);
        loadWorldData(fileMap);
        loadChestData(fileMap);
        loadInventorySlotsData(fileMap);
        loadEquipmentSlotsData(fileMap);
    }

    private static void loadPlayerData(HashMap<String, Object> fileMap) {
        StatePlaying.player.x = (int) fileMap.get("PlayerX");
        StatePlaying.player.y = (int) fileMap.get("PlayerY");
        Player.health = Player.healthMAX;
    }

    private static void loadWorldData(HashMap<String, Object> fileMap) throws SlickException {
        World.init((String) fileMap.get("Level"));
    }

    private static void loadInventorySlotsData(HashMap<String, Object> fileMap) throws SlickException {
        for (int i = 0; i < Inventory.invSlots.length; i++) {
            for (int ii = 0; ii < Inventory.invSlots[i].length; ii++) {
                Inventory.invSlots[i][ii].itemStack = loadItemStack(fileMap,
                        "InventorySlot[" + i + "][" + ii + "]");
            }
        }
    }

    private static void loadEquipmentSlotsData(HashMap<String, Object> fileMap) throws SlickException {
        for (int i = 0; i < Inventory.equipSlots.length; i++) {
            Inventory.equipSlots[i].itemStack = loadItemStack(fileMap, "EquipmentSlot[" + i + "]");
        }
    }

    private static ItemStack loadItemStack(HashMap<String, Object> fileMap, String ID) throws SlickException {
        String itemName = (String) fileMap.get(ID + ".Item");
        int amount = 1;

        if (!itemName.equals("none")) {
            if (fileMap.containsKey(ID + ".Amount")) {
                amount = (int) fileMap.get(ID + ".Amount");
            }

            if (!itemName.equals("Error: No path found.")) {
                Item item = loadItem(itemName);
                return new ItemStack(item, amount);
            }
        }

        return null;
    }

    private static void loadChestData(HashMap<String, Object> fileMap) {
        int chestNum = (int) fileMap.get("NumberOpenedChests");
        for (int i = 0; i < chestNum; i++) {
            String chestID = (String) fileMap.get("OpenedChestID[" + i + "]");
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
                bufferedWriter.newLine();

                saveWorldData(bufferedWriter);
                bufferedWriter.newLine();

                saveChestData(bufferedWriter);
                bufferedWriter.newLine();

                saveInventorySlotsData(bufferedWriter);
                saveEquipmentSlotsData(bufferedWriter);

                bufferedWriter.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void savePlayerData(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("PlayerX: " + Integer.toString((int) StatePlaying.player.x));
        bufferedWriter.newLine();
        bufferedWriter.write("PlayerY: " + Integer.toString((int) StatePlaying.player.y));
        bufferedWriter.newLine();
    }

    private static void saveWorldData(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("Level: " + World.levelName);
        bufferedWriter.newLine();
    }

    private static void saveChestData(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("NumberOpenedChests: " + Integer.toString(World.openedChestList.size()));
        bufferedWriter.newLine();

        Object[] IDs = World.openedChestList.toArray();

        for (int i = 0; i < IDs.length; i++) {
            bufferedWriter.write("OpenedChestID[" + i + "]: " + IDs[i]);
            bufferedWriter.newLine();
        }
    }

    private static void saveInventorySlotsData(BufferedWriter bufferedWriter) throws IOException {
        for (int i = 0; i < Inventory.invSlots.length; i++) {
            for (int ii = 0; ii < Inventory.invSlots[i].length; ii++) {
                saveItemStack(bufferedWriter, Inventory.invSlots[i][ii].itemStack, "InventorySlot[" + i + "][" + ii + "]");
            }

            bufferedWriter.newLine();
        }
    }

    private static void saveEquipmentSlotsData(BufferedWriter bufferedWriter) throws IOException {
        for (int i = 0; i < Inventory.equipSlots.length; i++) {
            saveItemStack(bufferedWriter, Inventory.equipSlots[i].itemStack, "EquipmentSlot[" + i + "]");
        }
    }

    private static void saveItemStack(BufferedWriter bufferedWriter, ItemStack itemStack, String ID) throws IOException {
        if (itemStack != null) {
            bufferedWriter.write(ID + ".Item: " + itemStack.item.path);
            bufferedWriter.newLine();
            if (itemStack.amount > 1) {
                bufferedWriter.write(ID + ".Amount: " + itemStack.amount);
                bufferedWriter.newLine();
            }
        } else {
            bufferedWriter.write(ID + ".Item: " + "none");
            bufferedWriter.newLine();
        }
    }

    public static Item loadItem(String path) throws SlickException {
        HashMap<String, Object> fileMap = loadHashMap(path);

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

            case "RingItem": {
                int effect = RingItem.NONE;
                if (fileMap.get("Effect").equals("SPEED")) {
                    effect = RingItem.SPEED;
                }

                return new RingItem(name, path, sprite, icon, effect);
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

        return null;
    }

    public static Enemy loadEnemy(String path) throws SlickException {
        HashMap<String, Object> fileMap = loadHashMap(path);

        String type = (String) fileMap.get("Type");
        Animation sprite = loadSprite((String) fileMap.get("Sprite"));
        int MAX_HEALTH = (int) fileMap.get("MAX_HEALTH");
        int MAX_SPEED = (int) fileMap.get("MAX_SPEED");
        float ACCELERATION = (float) ((double) fileMap.get("ACCELERATION"));
        int defense = (int) fileMap.get("Defense");
        int stability = (int) fileMap.get("Stability");
        int damage = (int) fileMap.get("Damage");
        int knockback = (int) fileMap.get("Knockback");

        switch (type) {
            case "ZombieAI": {
                int JUMP_SPEED = (int) fileMap.get("JUMP_SPEED");

                return new ZombieAI(sprite, 0, 0, false, MAX_HEALTH, MAX_SPEED,
                        JUMP_SPEED, ACCELERATION, defense, stability, damage, knockback);
            }

            case "FlyingAI": {
                
                return new FlyingAI(sprite, 0, 0, false, MAX_HEALTH, MAX_SPEED,
                        ACCELERATION, defense, stability, damage, knockback);
            }
        }

        return null;
    }

    public static Animation loadSprite(String path) throws SlickException {
        HashMap<String, Object> fileMap = loadHashMap(path);

        String image = (String) fileMap.get("Image");
        int width = (int) fileMap.get("Width");
        int height = (int) fileMap.get("Height");
        int duration = (int) fileMap.get("Duration");

        SpriteSheet spriteSheet = new SpriteSheet(image, width, height);
        Animation sprite = new Animation(spriteSheet, duration);

        return sprite;
    }

    private static HashMap<String, Object> loadHashMap(String path) {
        HashMap<String, Object> fileMap = null;

        try {
            FileReader fileReader = new FileReader(path);
            Yaml yaml = new Yaml();
            fileMap = (HashMap<String, Object>) yaml.load(fileReader);

            try {
                fileReader.close();
            } catch (IOException ex) {
                Logger.getLogger(DataIO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fileMap;
    }
}

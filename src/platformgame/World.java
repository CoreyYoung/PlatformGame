package platformgame;

import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import platformgame.enemies.EnemyHandler;
import platformgame.inventory.ItemStack;
abstract public class World {

    private static int[][] tileMap;
    private static int levelWidth;
    private static int levelHeight;

    public static TiledMap level;
    public static Image background;
    public static String levelName;

	public static final int BLOCK_WIDTH = 32;
    public static final float GRAVITY = 0.3f;
    public static final int yspeedMAX = 10;
    public static final int LEFT_SLOPE = 3;
    public static final int RIGHT_SLOPE = 2;

    public static ArrayList<String> openedChestList = new ArrayList<>();
    public static ArrayList<Door> doorList = new ArrayList<>();
    public static ArrayList<Sign> signList = new ArrayList<>();
    public static ArrayList<Spikes> spikesList = new ArrayList<>();

    public static void init(String path) throws SlickException {
        clearWorld();
        loadLevel(path);
        EnemyHandler.init();
        Sign.init();
        Door.init();
        Spikes.init();
        Chest.init();
        FloppyDisk.init();
    }

    public static void render(int camX, int camY) {
        if (background != null) {
            background.draw();
        }

        Chest.render(camX, camY);
        FloppyDisk.render(camX, camY);

        if (level != null) {
            level.render(camX, camY, 0, 0, 640, 480);
        }

        for (Door door : doorList) {
            door.render(camX, camY);
        }

        for (Sign sign : signList) {
            sign.render(camX, camY);
        }

        for (Spikes spikes : spikesList) {
            spikes.render(camX, camY);
        }
    }

    public static void loadLevel(String path) throws SlickException {
        levelName = path;
        level = new TiledMap(path);

        tileMap = new int[level.getWidth()][level.getHeight()];
        levelWidth = level.getWidth() * BLOCK_WIDTH;
        levelHeight = level.getHeight() * BLOCK_WIDTH;

        for (int i = 0; i < level.getWidth(); i++) {
            for (int ii = 0; ii < level.getHeight(); ii++) {
                tileMap[i][ii] = (level.getTileId(i, ii, 0));
            }
        }

        createBackground();
        createDoors();
        createSigns();
        createSpikes();
        createChests();
        createFloppyDisks();
    }

    public static boolean isBlockAtPoint(int x, int y) {
        return (tileMap[x][y] == 1);
    }

    public static boolean isBlockInLine(int x1, int x2, int y1, int y2) {
        if (x1 == x2) {
            for (int i = y1; i <= y2; i++) {
                if (tileMap[x1][i] == 1) {
                    return true;
                }
            }
        } else if (y1 == y2) {
            for (int i = x1; i <= x2; i++) {
                if (tileMap[i][y1] == 1) {
                    return true;
                }
            }
        } else {
            System.err.println("World.isBlockInLine received invalid parameters.");
            return false;
        }

        return false;
    }

    public static int getTileAtPoint(int x, int y) {
        return tileMap[x][y];
    }

    private static void createBackground() throws SlickException {
        String backgroundPath = level.getMapProperty("Background", "");
        if (!backgroundPath.equals("")) {
            background = new Image(backgroundPath);
        } else {
            background = new Image("/data/graphics/Background.gif");
        }
    }

    private static void createDoors() {
        for (int i = 0; i < level.getObjectCount(0); i++) {
            if (level.getObjectType(0, i).equals("Door")) {
                int x = level.getObjectX(0, i);
                int y = level.getObjectY(0, i);
                String path = level.getObjectProperty(0, i, "path", "");

                doorList.add(new Door(x, y, path));
            }
        }
    }

    private static void createSigns() {
        for (int i = 0; i < level.getObjectCount(0); i++) {
            if (level.getObjectType(0, i).equals("Sign")) {
                int x = level.getObjectX(0, i);
                int y = level.getObjectY(0, i);
                String path = level.getObjectProperty(0, i, "message", "");

                signList.add(new Sign(x, y, path));
            }
        }
    }

    private static void createSpikes() {
        for (int i = 0; i < level.getObjectCount(0); i++) {
            if (level.getObjectType(0, i).equals("Spikes")) {
                int x = level.getObjectX(0, i);
                int y = level.getObjectY(0, i);

                spikesList.add(new Spikes(x, y));
            }
        }
    }

    private static void createChests() {
        for (int i = 0; i < level.getObjectCount(0); i++) {
            if (level.getObjectType(0, i).equals("Chest")) {
                int x = level.getObjectX(0, i);
                int y = level.getObjectY(0, i);
                ItemStack itemStack = null;
                String itemName = level.getObjectProperty(0, i, "item", "");
                Integer amount;

                try {
                    amount = Integer.parseInt(level.getObjectProperty(0, i, "amount", "1"));
                } catch (NumberFormatException e) {
                    System.err.println("Error: Chest property 'amount' is incorrect!");
                    amount = 0;
                }

                try {
                    itemStack = new ItemStack(DataIO.loadItem(itemName), amount);
                } catch (SlickException e) {
                    System.err.println("Error: Chest property 'item' is incorrect!");
                }

                if (itemStack != null) {
                    Chest.createChest(x, y, itemStack);
                }
            }
        }
    }

    private static void createFloppyDisks() {
        for (int i = 0; i < level.getObjectCount(0); i++) {
            if (level.getObjectType(0, i).equals("FloppyDisk")) {
                int x = level.getObjectX(0, i);
                int y = level.getObjectY(0, i);
                FloppyDisk.floppyDiskList.add(new FloppyDisk(x, y));
            }
        }
    }

    private static void clearWorld() {
        EnemyHandler.clearEnemies();
        clearDoors();
        clearSigns();
        clearSpikes();
        clearChests();
        clearFloppyDisks();
        clearTileMap();
    }

    private static void clearDoors() {
        doorList.clear();
    }

    private static void clearSigns() {
        signList.clear();
    }

    private static void clearSpikes() {
        spikesList.clear();
    }

    private static void clearChests() {
        Iterator<Chest> iterator = Chest.chestList.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    private static void clearFloppyDisks() {
        Iterator<FloppyDisk> iterator = FloppyDisk.floppyDiskList.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    private static void clearTileMap() {
        tileMap = null;
    }

    public static boolean isPointInLevel(int x, int y) {
        return (x >= 0 && x <= levelWidth && y >= 0 && y <= levelHeight);
    }

    public static boolean isChestOpened(Chest chest) {
        return openedChestList.contains(levelName + chest.x + chest.y);
    }

    public static void setChestOpened(Chest chest) {
        openedChestList.add(levelName + chest.x + chest.y);
    }

    public static int getWidth() {
        return levelWidth;
    }

    public static int getHeight() {
        return levelHeight;
    }
}

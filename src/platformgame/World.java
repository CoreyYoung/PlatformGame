package platformgame;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

abstract public class World {
    static TiledMap level;
    static int levelWidth;
    static int levelHeight;
    static int blockSize = 32;
    static final int doorMAX = 10;
    static Door[] door = new Door[doorMAX];
    static final int signMAX = 10;
    static Sign[] sign = new Sign[signMAX];
    static boolean[][] tileMap;
    static final float GRAVITY = 0.3f;
    static final int yspeedMAX = 10;
    static String levelName;
    
    static void init(String path) throws SlickException {
        clearWorld();
        loadLevel(path);
        Enemy.init();
        Sign.init();
        Door.init();
    }
    
    static void render(int camX, int camY) {
        if (level != null) {
            level.render(camX, camY, 0, 0, 640,480);
        }
        
        for (int i = 0; i < doorMAX; i++) {
            if (door[i] != null) {
                door[i].render(camX, camY);
            }
        }
        
        for (int i = 0; i < signMAX; i ++) {
            if (sign[i] != null) {
                sign[i].render(camX, camY);
            }
        }
    }
    
    static void loadLevel(String path) throws SlickException {
        levelName = path;
        level = new TiledMap(path);
        tileMap = new boolean[level.getWidth()] [level.getHeight()];
        levelWidth = level.getWidth()*blockSize;
        levelHeight = level.getHeight()*blockSize;
        
        for (int i = 0; i < level.getWidth(); i ++) {
            for (int ii = 0; ii < level.getHeight(); ii ++) {
                tileMap[i][ii] = (level.getTileId(i, ii, 0) != 0);
            }
        }
        
        createDoors();
        createSigns();
    }
    
    static boolean tileAtPosition(int x, int y) {
        return tileMap[x][y];
    }
    
    static void createDoors() {
        int num = 0;
        for (int i = 0; i < level.getObjectCount(0); i ++) {
            if (level.getObjectType(0, i).equals("Door")) {
                if (num < doorMAX) {
                    door[num] = new Door(
                            level.getObjectX(0, i),
                            level.getObjectY(0, i),
                            level.getObjectProperty(0, i, "path", ""));
                    num ++;
                }
            }
        }
    }
    
    static void createSigns() {
        int num = 0;
        for (int i = 0; i < level.getObjectCount(0); i ++) {
            if (level.getObjectType(0, i).equals("Sign")) {
                if (num < signMAX) {
                    sign[num] = new Sign(
                            level.getObjectX(0, i),
                            level.getObjectY(0, i),
                            level.getObjectProperty(0, i, "message", ""));
                    num ++;
                }
            }
        }
    }
    
    static void clearWorld() {
        Enemy.clearEnemies();
        clearDoors();
        clearSigns();
        clearTileMap();
    }
    
    static void clearDoors() {
        for (int i = 0; i < doorMAX; i ++) {
            door[i] = null;
        }
    }
    
    static void clearSigns() {
        for (int i = 0; i < signMAX; i ++) {
            sign[i] = null;
        }
    }
    
    static void clearTileMap() {
        tileMap = null;
    }
}

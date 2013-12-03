package platformgame;

import org.newdawn.slick.SlickException;

abstract public class Enemy {    
    private static final int zombieMAX = 10;
    
    public static Zombie[] zombie = new Zombie[zombieMAX];
    
    public static void init() throws SlickException {
        Zombie.init();
        spawnZombies();
    }
    
    public static void update() {
        for (int i = 0; i < zombieMAX; i ++) {
            if (zombie[i] != null && zombie[i].awake) {
                zombie[i].update();
            }
        }
    }
    
    public static void render(int camX, int camY) {
        for (int i = 0; i < zombieMAX; i ++) {
            if (zombie[i] != null && zombie[i].awake) {
                zombie[i].render(camX, camY);
            }
        }
    }
    
    public static void clearEnemies() {
        clearZombies();
    }
    
    private static void spawnZombies() {
        int x;
        int y;
        for (int i = 0; i < World.level.getObjectCount(0) ; i ++) {
            if (World.level.getObjectType(0, i).equals("Zombie")) {
                for (int ii = 0; ii < zombieMAX; ii ++) {
                    if (zombie[ii] == null) {
                        x = World.level.getObjectX(0, i);
                        y = World.level.getObjectY(0, i);
                        createZombie(x, y, ii);
                        break;
                    }
                }
            }
        }
    }
    
    private static void createZombie(int x, int y, int id) {
        for (int i = 0; i < zombieMAX; i ++) {
            if (zombie[i] == null) {
                zombie[i] = new Zombie(x, y, id, false);
                break;
            }
        }
    }
    
    private static void clearZombies() {
        for (int i = 0; i < zombieMAX; i ++) {
            zombie[i] = null;
        }
    }
}
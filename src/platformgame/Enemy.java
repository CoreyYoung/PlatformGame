package platformgame;

import org.newdawn.slick.SlickException;

abstract public class Enemy {    
    static public final int zombieMAX = 10;
    static public Zombie[] zombie = new Zombie[zombieMAX];
    
    static void init() throws SlickException {
        Zombie.init();
        spawnZombies();
    }
    
    static void update() {
        for (int i = 0; i < zombieMAX; i ++) {
            if (zombie[i] != null && zombie[i].awake) {
                zombie[i].update();
            }
        }
    }
    
    static void render(int camX, int camY) {
        for (int i = 0; i < zombieMAX; i ++) {
            if (zombie[i] != null && zombie[i].awake) {
                zombie[i].render(camX, camY);
            }
        }
    }
    
    static void spawnZombies() {
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
    
    static void createZombie(int x, int y, int id) {
        for (int i = 0; i < zombieMAX; i ++) {
            if (zombie[i] == null) {
                zombie[i] = new Zombie(x, y, id, false);
                break;
            }
        }
    }
    
    static void clearEnemies() {
        clearZombies();
    }
    
    static void clearZombies() {
        for (int i = 0; i < zombieMAX; i ++) {
            zombie[i] = null;
        }
    }
}
package platformgame;

import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.SlickException;

abstract public class Enemy {
    public static ArrayList<ZombieAI> zombieList = new ArrayList<>();

    public static void init() throws SlickException {
        spawnZombies();
    }

    public static void update() {
        Iterator iterator = zombieList.iterator();
        while (iterator.hasNext()) {
            ZombieAI zombie = (ZombieAI) iterator.next();
            if (zombie.health <= 0) {
                iterator.remove();
            }
        }
        
        for (ZombieAI zombie : zombieList) {
            if (zombie != null) {
                if (zombie.awake) {
                    zombie.update();
                }
            }
        }
    }

    public static void render(int camX, int camY) {
        for (ZombieAI zombie : zombieList) {
            if (zombie != null) {
                if (zombie.awake) {
                    zombie.render(camX, camY);
                }
            }
        }
    }

    public static void clearEnemies() {
        clearZombies();
    }

    private static void spawnZombies() throws SlickException {
        int x;
        int y;
        for (int i = 0; i < World.level.getObjectCount(0); i++) {
            if (World.level.getObjectType(0, i).equals("Zombie")) {
                x = World.level.getObjectX(0, i);
                y = World.level.getObjectY(0, i);
                createZombie(x, y);
            }
        }
    }

    private static void createZombie(int x, int y) throws SlickException {
        ZombieAI zombie = DataIO.loadEnemy("data/enemies/Zombie.txt");
        zombie.x = x;
        zombie.y = y;
        zombieList.add(zombie);
    }

    private static void clearZombies() {
        Iterator iterator = zombieList.iterator();
        
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
}
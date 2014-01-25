package platformgame.Enemies;

import platformgame.Enemies.ZombieAI;
import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.SlickException;
import platformgame.DataIO;
import platformgame.World;

abstract public class EnemyHandler {
    public static ArrayList<ZombieAI> zombieList = new ArrayList<>();

    public static void init() throws SlickException {
        spawnEnemies();
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
        Iterator iterator = zombieList.iterator();
        
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    private static void spawnEnemies() throws SlickException {
        int x;
        int y;
        for (int i = 0; i < World.level.getObjectCount(0); i++) {
            if (World.level.getObjectType(0, i).equals("Enemy")) {
                String path = World.level.getObjectProperty(0, i, "Type", null);
                x = World.level.getObjectX(0, i);
                y = World.level.getObjectY(0, i);
                createEnemy(x, y, path);
            }
        }
    }

    private static void createEnemy(int x, int y, String path) throws SlickException {
        ZombieAI zombie = DataIO.loadEnemy(path);
        zombie.x = x;
        zombie.y = y;
        zombieList.add(zombie);
    }
}
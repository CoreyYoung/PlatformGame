package platformgame.enemies;

import platformgame.enemies.ZombieAI;
import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.SlickException;
import platformgame.DataIO;
import platformgame.World;

abstract public class EnemyHandler {
    public static ArrayList<Enemy> enemyList = new ArrayList<>();

    public static void init() throws SlickException {
        spawnEnemies();
    }

    public static void update() {
        Iterator iterator = enemyList.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = (Enemy) iterator.next();
            if (enemy.health <= 0) {
                iterator.remove();
            }
        }
        
        for (Enemy enemy : enemyList) {
            if (enemy != null) {
                if (enemy.awake) {
                    enemy.update();
                }
            }
        }
    }

    public static void render(int camX, int camY) {
        for (Enemy enemy : enemyList) {
            if (enemy != null) {
                if (enemy.awake) {
                    enemy.render(camX, camY);
                }
            }
        }
    }

    public static void clearEnemies() {
        Iterator iterator = enemyList.iterator();
        
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
        Enemy enemy = DataIO.loadEnemy(path);
        enemy.x = x;
        enemy.y = y;
        enemyList.add(enemy);
    }
}
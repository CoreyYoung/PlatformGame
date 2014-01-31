package platformgame;

import platformgame.enemies.EnemyHandler;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import platformgame.enemies.Enemy;

abstract public class Camera {

    private static final int cameraWidth = Game.width;
    private static final int cameraHeight = Game.height;
    public static int x = 0;
    public static int y = 0;
    private static int xOffset = 0;
    private static int yOffset = 0;
    private static final int yOffsetTimerMAX = 30;
    private static int yOffsetTimer = yOffsetTimerMAX;

    static void update(Input input) {
        movement(input);
        wakeObjects();
    }

    static void render(Graphics g, Input input) {
        World.render(-x, -y);

        EnemyHandler.render(-x, -y);
        Player.render(-x, -y, g);

        CombatSystem.render(input, -x, -y);

        for (Projectile projectile : Projectile.projectileList) {
            projectile.render(-x, -y, g);
        }

        Hud.render(g, input);
    }

    private static void movement(Input input) {
        //x = inRange((int)Player.x-(cameraWidth/2), 0, World.levelWidth-cameraWidth);
        //y = inRange((int)Player.y-(cameraHeight/2), 0, World.levelHeight-cameraHeight);
        elasticCamera(input);
        //int shake = 8;
        //x += (Math.random()*shake)-(shake/2);
        //y += (Math.random()*shake)-(shake/2);

    }

    private static void wakeObjects() {
        for (Enemy enemy : EnemyHandler.enemyList) {
            if (enemy != null && !enemy.awake) {
                if (x <= enemy.x + 32 && x + cameraWidth >= enemy.x
                        && y <= enemy.y + 64 && y + cameraHeight >= enemy.y) {
                    enemy.awake = true;
                }
            }
        }
    }

    private static int inRange(int val, int min, int max) {
        val = Math.min(val, max);
        val = Math.max(val, min);
        return val;
    }

    private static void elasticCamera(Input input) {
        x = (int) ((Player.x + 16) - (cameraWidth / 2)) + xOffset;
        y = (int) ((Player.y + 32) - (cameraHeight / 2)) + yOffset;

        x = inRange(x, 0, World.getWidth() - cameraWidth);
        y = inRange(y, 0, World.getHeight() - cameraHeight);

        if (input.isKeyDown(Input.KEY_W)) {
            yOffsetTimer--;
            if (yOffsetTimer <= 0) {
                yOffset -= 2;
            }
        } else if (yOffset < 0) {
            yOffsetTimer = yOffsetTimerMAX;
            yOffset = Math.min(yOffset + 4, 0);
        }

        if (input.isKeyDown(Input.KEY_S)) {
            yOffsetTimer--;
            if (yOffsetTimer <= 0) {
                yOffset += 2;
            }
        } else if (yOffset > 0) {
            yOffsetTimer = yOffsetTimerMAX;
            yOffset = Math.max(yOffset - 4, 0);
        }

        if (Player.dir == 0 && xOffset < 180) {
            xOffset += 3;
        } else if (Player.dir == 180 && xOffset > -180) {
            xOffset -= 3;
        }

        xOffset = inRange(xOffset, -(128), 128);
        yOffset = inRange(yOffset, -128, 128);
    }
}

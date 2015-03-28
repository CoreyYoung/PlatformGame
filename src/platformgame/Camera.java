package platformgame;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import platformgame.enemies.Enemy;
import platformgame.enemies.EnemyHandler;

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
        StatePlaying.player.render(-x, -y, g);

        CombatSystem.render(input, -x, -y);

        for (Projectile projectile : Projectile.projectileList) {
            projectile.render(-x, -y, g);
        }

        Hud.render(g, input);
    }

    private static void movement(Input input) {
        y = (int) ((StatePlaying.player.y + StatePlaying.player.height / 2) - (cameraHeight / 2)) + yOffset;
        y = inRange(y, 0, World.getHeight() - cameraHeight);
		
		x = (int) ((StatePlaying.player.x + StatePlaying.player.width / 2) - (cameraWidth / 2)) + xOffset;
		x = inRange(x, 0, World.getWidth() - cameraWidth);

		int target;
		
        if (Player.dir == 0) {
			target = cameraWidth / 2 - 180;
        } else if (Player.dir == 180) {
            target = -(cameraWidth / 2 - 180);
        } else {
			target = 0;
		}
		
		if (input.isKeyDown(Game.INPUT_DOWN)) {
			yOffsetTimer --;
			
			if (yOffsetTimer <= 0) {
				yOffset += 2;
			}
		} else if (input.isKeyDown(Game.INPUT_UP)) {
			yOffsetTimer --;
			
			if (yOffsetTimer <= 0) {
				yOffset -= 2;
			}
		} else {
			yOffsetTimer = 0;
			
			if (yOffset != 0) {
				yOffset -= Math.signum(yOffset) * 2;
			}
		}
		
		if (xOffset < target) {
			xOffset = Math.min(xOffset + 3, target);
		} else if (xOffset > target) {
			xOffset = Math.max(xOffset - 3, target);
		}
		
		int playerCenter = (int) (StatePlaying.player.x + StatePlaying.player.width / 2);
		int min = cameraWidth / 2 - playerCenter;
		int max = World.getWidth() - (playerCenter + cameraWidth / 2);

		xOffset = inRange(xOffset, min, max);
		
        yOffset = inRange(yOffset, -128, 128);
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
}

package platformgame;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import platformgame.inventory.Inventory;
import platformgame.inventory.RingItem;

public class Player extends MovingEntity {

    private static final int MAX_XSPEED = 3;
    private static final int JUMPSPEED = -8;
    private static final float ACCELERATION = 0.1f;
    private static final float FRICTION = 0.07f;
    private static Animation sprStand;
    private static Animation sprWalk;
    private static Animation sprHurt;
    private static SpriteSheet spriteSheet;

    public static final int invincibilityTimerMAX = 60;
    public static int invincibilityTimer = 0;
    public static boolean invincible = false;
    public static final int healthMAX = 10;
    public static int health = healthMAX;
    public static int dir = 0;

    public Player() throws SlickException {
        width = 32;
        height = 64;
        spriteSheet = new SpriteSheet("data/graphics/Player.gif", width, height);
        sprStand = new Animation(spriteSheet, 0, 0, 0, 0, false, 1000 * 1, true);
        sprWalk = new Animation(spriteSheet, 0, 0, 3, 0, true, (int) (1000 * 0.25), true);
        sprHurt = new Animation(spriteSheet, 0, 1, 3, 1, true, (int) (1000 * 0.1), true);
    }

    public void update(Input input) throws SlickException {
        movement(input);
        interact(input); //checks context sensitive 'interact' button (talk, pickup item, use door etc.)

        if (invincibilityTimer <= 0) {
            invincible = false;
        } else {
            invincibilityTimer--;
        }
    }

    public void render(int camX, int camY, Graphics g) {
        Animation sprite;
        if (invincible) {
            sprite = sprHurt;
        } else {
            if (velocity.getXMagnitude() == 0) {
                sprite = sprStand;
            } else {
                sprite = sprWalk;
            }
        }

        if (dir == 180) {
            sprite.draw((int) x + 32 + camX, y + camY, -width, height);
        } else {
            sprite.draw((int) x + camX, y + camY);
        }
    }

    private void movement(Input input) {
        int tempMAX_XSPEED = MAX_XSPEED;
        if (Inventory.getRingItem() != null) {
            if (Inventory.getRingItem().effect == RingItem.SPEED) {
                tempMAX_XSPEED = 5;
            }
        }

        if (input.isKeyDown(Input.KEY_A)) {
            if (input.isKeyPressed(Input.KEY_A) || !input.isKeyDown(Input.KEY_D)) {
                dir = 180;
            }

            velocity.setXMagnitude(Math.max(velocity.getXMagnitude() - ACCELERATION, -tempMAX_XSPEED));

        } else {
            if (velocity.getXMagnitude() < 0) {
                velocity.setXMagnitude(Math.min(velocity.getXMagnitude() + FRICTION, 0));
            }
        }

        if (input.isKeyDown(Input.KEY_D)) {
            if (input.isKeyPressed(Input.KEY_D) || !input.isKeyDown(Input.KEY_A)) {
                dir = 0;
            }

            velocity.setXMagnitude(Math.min(velocity.getXMagnitude() + ACCELERATION, tempMAX_XSPEED));

        } else {
            if (velocity.getXMagnitude() > 0) {
                velocity.setXMagnitude(Math.max(velocity.getXMagnitude() - FRICTION, 0));
            }
        }

        if (input.isKeyPressed(Input.KEY_SPACE)) {
            if ((World.isBlockAtPoint((int) (x / 32), (int) ((y + 64 + 1) / 32))
                    && !World.isBlockAtPoint((int) (x / 32), (int) ((y + 32 + 1) / 32)))
                    || (World.isBlockAtPoint((int) ((x + 32 - 1) / 32), (int) ((y + 64 + 1) / 32))
                    && !World.isBlockAtPoint((int) ((x + 32 - 1) / 32), (int) ((y + 32 + 1) / 32)))
                    || (World.getTileAtPoint((int) (x + 31) / 32, (int) (y + 64) / 32) == 2
                    || World.getTileAtPoint((int) x / 32, (int) (y + 64) / 32) == 3)) {

                velocity.setYMagnitude(JUMPSPEED);
            }
        } else if (!input.isKeyDown(Input.KEY_SPACE)) {
            if (velocity.getYMagnitude() < 0) {
                velocity.setYMagnitude(velocity.getYMagnitude() * 0.75f);
            }
        }

        if (velocity.getYMagnitude() < World.yspeedMAX) {
            velocity.setYMagnitude(velocity.getYMagnitude() + World.GRAVITY);
        }

        collisions();
    }

    @Override
    public void collisions() {
        super.collisions();

        if (!invincible) {
            for (Spikes spikes : World.spikesList) {
                if (spikes.x <= x + 32 && spikes.x + 32 >= x
                        && spikes.y <= y + 64 && spikes.y + 32 >= y) {
                    health -= Spikes.DAMAGE;
                    invincibilityTimer = invincibilityTimerMAX;
                    invincible = true;

                    if (x > spikes.x) {
                        velocity.setXMagnitude(MAX_XSPEED);
                    } else {
                        velocity.setXMagnitude(-MAX_XSPEED);
                    }

                    velocity.setYMagnitude(JUMPSPEED);
                }
            }
        }
    }

    private void interact(Input input) throws SlickException {
        if (input.isKeyPressed(Input.KEY_S)) {
            for (Door door : World.doorList) {
                if (door != null) {
                    if (x + 32 >= door.x + 16 && x < door.x + 16
                            && y + 64 >= door.y && y <= door.y + 64) {
                        openDoor(door.path);
                        break;
                    }
                }
            }

            for (Sign sign : World.signList) {
                if (sign != null) {
                    if (x + 32 >= sign.x + 16 && x <= sign.x + 16
                            && y + 64 >= sign.y && y <= sign.y + 32) {
                        readSign(sign);
                        break;
                    }
                }
            }

            for (Chest chest : Chest.chestList) {
                if (chest != null) {
                    if (x + 32 >= chest.x + 16 && x <= chest.x + 16
                            && y + 64 >= chest.y && y <= chest.y + 32) {
                        useChest(chest);
                        break;
                    }
                }
            }

            for (FloppyDisk floppyDisk : FloppyDisk.floppyDiskList) {
                if (floppyDisk != null) {
                    if (x + 32 >= floppyDisk.x + 16 && x <= floppyDisk.x + 16
                            && y + 64 >= floppyDisk.y && y <= floppyDisk.y + 32) {
                        floppyDisk.useFloppyDisk();
                        break;
                    }
                }
            }
        }
    }

    private void openDoor(String path) throws SlickException {
        String levelDeparting = World.levelName;
        World.init(path);

        velocity = new Vector(0, 0);

        for (Door door : World.doorList) {
            if (door != null) {
                if (door.path.equals(levelDeparting)) {
                    x = door.x;
                    y = door.y;
                    break;
                }
            }
        }
    }

    private void readSign(Sign sign) {
        Hud.addMessage(sign.message);
    }

    private static void useChest(Chest chest) throws SlickException {
        chest.useChest();
    }
}

package platformgame;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import platformgame.inventory.Inventory;

abstract public class Hud {

    private static final int messageMAX = 10;
    private static final String[] message = new String[messageMAX];
    private static Image textBox;
    private static int stringIndex = 0;

    public static void init() throws SlickException {
        textBox = new Image("data/graphics/gui/MessageBox.png");
    }

    public static void update(Input input) throws SlickException {
        if (Game.inventory) {
            Inventory.useInventory(input);
        }
        if (message[0] != null) {
            boolean escapePressed = input.isKeyPressed(Input.KEY_ESCAPE);
            boolean spacePressed = input.isKeyPressed(Game.INPUT_JUMP);
            boolean spaceDown = input.isKeyDown(Game.INPUT_JUMP);
            Game.paused = true;

            if (spaceDown) {
                for (int i = 0; i < 2; i++) {
                    if (stringIndex < message[0].length() - 1) {
                        stringIndex++;
                    }
                }
            } else if (escapePressed) {
                if (stringIndex != message[0].length() - 1) {
                    stringIndex = message[0].length() - 1;
                    escapePressed = false;
                }
            } else {
                if (stringIndex < message[0].length() - 1) {
                    stringIndex++;
                }
            }

            if (stringIndex >= message[0].length() - 1
                    && (spacePressed || escapePressed)) {
                removeMessage();
                Game.paused = false;
            }
        }
    }

    public static void render(Graphics g, Input input) {
        g.drawString("Health: " + Player.health
                + System.lineSeparator() + "Defense: " + Inventory.getDefense()
                + System.lineSeparator() + "Attack: " + Inventory.getAttack()
                + System.lineSeparator() + "Stability: " + Inventory.getStability(), 10, 32);

        if (message[0] != null) {
            textBox.draw(0, 0);
            drawMessage(g);
        }

        if (Game.inventory) {
            Inventory.render(input, g);
        }
    }

    private static void drawMessage(Graphics g) {
        g.drawString(formatString(message[0].substring(0, stringIndex + 1)), 32, 372);
    }

    public static void addMessage(String string) {
        for (int i = 0; i < messageMAX; i++) {
            if (message[i] == null) {
                message[i] = string;
                break;
            }
        }
    }

    private static void removeMessage() {
        stringIndex = 0;
        for (int i = 0; i < messageMAX - 1; i++) {
            if (message[i + 1] != null) {
                message[i] = message[i + 1];
            } else {
                message[i] = null;
                break;
            }
        }
        message[messageMAX - 1] = null;
    }

    private static String formatString(String str) {
        int spaceIndex = 0;
        int lineStart = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                spaceIndex = i;
            }

            if (i - lineStart == 65) {
                String first = str.substring(0, spaceIndex);
                String seperator = System.lineSeparator();
                String last = str.substring(spaceIndex + 1, str.length() - 1);

                str = first.concat(seperator).concat(last);

                lineStart = spaceIndex + 1;
            }
        }
        return String.valueOf(str);
    }
}

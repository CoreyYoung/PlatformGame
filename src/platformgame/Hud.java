package platformgame;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import platformgame.inventory.Inventory;

abstract public class Hud {
    static final int messageMAX = 10;
    static String[] message = new String[messageMAX];
    static Image textBox;
    static int stringIndex = 0;
    
    static void init() throws SlickException {
        textBox = new Image("data/graphics/textBox.gif");
    }
    
    static void update(Input input) {
        if (Game.inventory) {
            Inventory.useInventory(input);
        }
        if (message[0] != null) {
            Game.paused = true;
            
            if (input.isKeyDown(Input.KEY_Z) || input.isKeyDown(Input.KEY_X)) {
                for (int i = 0; i < 2; i ++) {
                    if (stringIndex < message[0].length()-1) {
                        stringIndex ++;
                   }
                }
            } else {
                if (stringIndex < message[0].length()-1) {
                    stringIndex ++;
                }
            }
            if (stringIndex >= message[0].length()-1
                    && (input.isKeyPressed(Input.KEY_Z) || input.isKeyPressed(Input.KEY_X))) {
                removeMessage();
                Game.paused = false;
            }
        }
    }
    
    static void render(Graphics g, Input input) {
        g.drawString("Health: "+Player.health
                        +System.lineSeparator()+"Defense: "+Inventory.getDefense()
                        +System.lineSeparator()+"Attack: "+Inventory.getAttack()
                        +System.lineSeparator()+"Stability: "+Inventory.getStability(), 10, 32);
        
        if (message[0] != null) {
            textBox.draw(0, 480-128);
            drawMessage(g);
        }
        
        if (Game.inventory) {
            Inventory.render(input);
        }
    }
    
    static void drawMessage(Graphics g) {
        g.drawString(formatString(message[0].substring(0, stringIndex)), 10, 480-128);
    }
    
    static void addMessage(String string) {
        for (int i = 0; i < messageMAX; i ++) {
            if (message[i] == null) {
                message[i] = string;
                break;
            }
        }
    }
    
    static void removeMessage() {
        stringIndex = 0;
        for (int i = 0; i < messageMAX-1; i ++) {
            if (message[i+1] != null) {
                message[i] = message[i+1];
            } else {
                message[i] = null;
                break;
            }
        }
        message[messageMAX-1] = null;
    }
    
    static String formatString(String str) {
        int spaceIndex = 0;
        int lineStart = 0;
        char[] result = new char[str.length()];
        for (int i = 0; i < str.length(); i ++) {
            if (str.charAt(i) == ' ') {
                spaceIndex = i;
            }

            if (i-lineStart == 65) {
                result[spaceIndex] = '\n';
                lineStart = spaceIndex+1;
            }
            
            result[i] = str.charAt(i);
        }
        return String.valueOf(result);
    }
}

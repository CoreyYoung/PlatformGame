package platformgame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import platformgame.inventory.Inventory;


public class Game extends BasicGame {
    static int width = 640;
    static int height = 480;
    
    static boolean fullscreen = false;
    static boolean showFPS = true;
    static String title = "Sandbox Platform Action? Game";
    static int fpslimit = 60;
    static boolean vSync = true;
    static boolean paused = false;
    static boolean inventory = false;
    Input input;
    
    public Game(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        //testMap = new TiledMap("data/testMap.tmx");
        Hud.init();
        World.init("data/levels/hub.tmx");
        Player.init();
        Inventory.init();
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        input = gc.getInput();
        Hud.update(input);
        
        if (!paused) {
            CombatSystem.update(input);
            Player.update(input);
            Inventory.update();
            Camera.update(input);
            Enemy.update();
            Projectile.update();
        }
        
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            gc.exit();
        }
        if (input.isKeyPressed(Input.KEY_P)) {
            paused = !paused;
        }
        if (input.isKeyPressed(Input.KEY_I)) {
            if (inventory) {
                inventory = false;
                paused = false;
            } else {
                inventory = true;
                paused = true;
            }
        }
        input.clearKeyPressedRecord();
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        Camera.render(g, input);
    }
    
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game(title));
        app.setDisplayMode(width, height, fullscreen);
        app.setSmoothDeltas(true);
        app.setTargetFrameRate(fpslimit);
        app.setVSync(vSync);
        app.setShowFPS(showFPS);
        app.start();
    }
}
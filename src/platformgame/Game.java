package platformgame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class Game extends StateBasedGame {
    private static final boolean fullscreen = false;
    private static final boolean showFPS = true;
    private static final boolean vSync = true;
    private static final String title = "Sandbox Platform Action? Game";
    private static final int fpslimit = 60;
    
    public static final int STATE_TITLE_SCREEN = 0;
    public static final int STATE_PLAYING = 1;
    public static final int width = 640;
    public static final int height = 480;
    
    public static boolean paused = false;
    public static boolean inventory = false;
    
    public Game(String title) {
        super(title);
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

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new StateTitleScreen(STATE_TITLE_SCREEN));
        this.addState(new StatePlaying(STATE_PLAYING));
    }
}
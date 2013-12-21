package platformgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StateDeathScreen extends BasicGameState {
    private static Image deathScreen;
    
    private final int stateID;
    
    public StateDeathScreen(int stateID) {
        this.stateID = stateID;
    }
    
    @Override
    public int getID() {
        return stateID;
    }
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        deathScreen = new Image("data/graphics/DeathScreen.png");
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        deathScreen.draw();
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        
    }
}
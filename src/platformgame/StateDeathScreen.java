package platformgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StateDeathScreen extends BasicGameState {

    private static Image deathScreen;
    private static Button playGame;
    private static Button exitGame;

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
        deathScreen = new Image("data/graphics/gui/DeathScreen.png");
        playGame = new Button(320 - 128, 32 * 5, "data/graphics/gui/NormalButtonPlayGame.png",
                "data/graphics/gui/HoverButtonPlayGame.png");
        exitGame = new Button(320 - 128, 32 * 8, "data/graphics/gui/NormalButtonExitGame.png",
                "data/graphics/gui/HoverButtonExitGame.png");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        deathScreen.draw();
        playGame.render(container.getInput());
        exitGame.render(container.getInput());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (playGame.isPressed(container.getInput())) {
            game.enterState(Game.STATE_PLAYING);
            DataIO.loadGame();
        }

        if (exitGame.isPressed(container.getInput())) {
            game.enterState(Game.STATE_PLAYING);
            System.exit(0);
        }
    }
}

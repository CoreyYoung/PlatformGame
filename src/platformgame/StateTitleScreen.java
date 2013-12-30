package platformgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.yaml.snakeyaml.Yaml;

public class StateTitleScreen extends BasicGameState {

    private Image backGroundImage;
    private Button playGame;
    private Button exitGame;
    private final int stateID;

    public StateTitleScreen(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        backGroundImage = new Image("data/graphics/TitleScreen.png");
        playGame = new Button(320 - 128, 32 * 5, "data/graphics/GUI/Menus/NormalButtonPlayGame.png",
                "data/graphics/GUI/Menus/HoverButtonPlayGame.png");
        exitGame = new Button(320 - 128, 32 * 8, "data/graphics/GUI/Menus/NormalButtonExitGame.png",
                "data/graphics/GUI/Menus/HoverButtonExitGame.png");
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        backGroundImage.draw();
        playGame.render(gc.getInput());
        exitGame.render(gc.getInput());
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        if (playGame.isPressed(gc.getInput())) {
            game.enterState(Game.STATE_PLAYING);
        }

        if (exitGame.isPressed(gc.getInput())) {
            System.exit(0);
        }
    }

}

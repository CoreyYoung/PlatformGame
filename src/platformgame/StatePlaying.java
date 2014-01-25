package platformgame;

import platformgame.Enemies.EnemyHandler;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import platformgame.inventory.Inventory;

public class StatePlaying extends BasicGameState {

    private final int stateID;
    private Button exit;
    private Input input;

    public StatePlaying(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        this.init(container, game);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        exit = new Button(640 - (128 + 16), 480 - (48 + 16), "data/graphics/gui/NormalButtonExit.png",
                "/data/graphics/gui/HoverButtonExit.png");
        Hud.init();
        World.init("data/levels/Hub.tmx");
        Player.init();
        Inventory.init();

        DataIO.loadGame();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Camera.render(g, input);

        if (Game.inventory) {
            exit.render(input);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        input = gc.getInput();
        Hud.update(input);

        if (!Game.paused) {
            if (Player.health <= 0) {
                game.enterState(Game.STATE_DEATH_SCREEN);
            }
            CombatSystem.update(input);
            Player.update(input);
            Inventory.update();
            Camera.update(input);
            EnemyHandler.update();
            Projectile.update();
        } else {
            if (exit.isPressed(input)) {
                game.enterState(Game.STATE_TITLE_SCREEN);
            }
        }

        if (input.isKeyPressed(Input.KEY_P)) {
            Game.paused = !Game.paused;
        }

        if (!Game.paused || Game.inventory) {
            if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                Game.inventory = !Game.inventory;
                Game.paused = Game.inventory;
            }
        }
        input.clearKeyPressedRecord();
    }
}
